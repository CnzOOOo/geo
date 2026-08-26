function localBridgeCandidates(wsPort?: string | number): string[] {
  const port = Number(wsPort || 9527);
  return [
    `http://127.0.0.1:${port + 1}`,
    `http://localhost:${port + 1}`,
    `http://127.0.0.1:${port}`,
    `http://localhost:${port}`,
  ];
}

async function fetchJson(url: string, init?: RequestInit, timeoutMs = 3000): Promise<any> {
  const controller = new AbortController();
  const timer = setTimeout(() => controller.abort(), timeoutMs);
  try {
    const res = await fetch(url, { ...init, cache: 'no-store', signal: controller.signal });
    if (!res.ok) {
      throw new Error(`HTTP ${res.status}`);
    }
    return await res.json();
  } finally {
    clearTimeout(timer);
  }
}

function sleep(ms: number): Promise<void> {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

async function resolveLocalBridgeBase(wsPort?: string | number): Promise<string> {
  let lastError: any = null;
  for (const base of localBridgeCandidates(wsPort)) {
    for (let attempt = 0; attempt < 3; attempt++) {
      try {
        await fetchJson(`${base}/status`, {}, 1500);
        return base;
      } catch (e) {
        lastError = e;
        await sleep(300);
      }
    }
  }
  throw lastError || new Error('本地 Wechatsync MCP 服务未启动');
}

export async function checkLocalWechatsync(wsPort?: string | number): Promise<{ connected: boolean }> {
  const base = await resolveLocalBridgeBase(wsPort);
  return await fetchJson(`${base}/status`, {}, 3000);
}

export async function publishViaLocalWechatsync(
  platforms: string[],
  title: string,
  markdown: string,
  wsPort?: string | number
): Promise<{ results?: Array<{ platform: string; success: boolean; postId?: string; postUrl?: string; error?: string }>; syncId?: string }> {
  const base = await resolveLocalBridgeBase(wsPort);
  const status = await fetchJson(`${base}/status`, {}, 3000);
  if (!status.connected) {
    throw new Error('Chrome 扩展未连接，请先检查扩展的同步桥接');
  }

  const content = markdownToHtml(markdown);
  const data = await fetchJson(
    `${base}/request`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        method: 'syncArticle',
        params: {
          platforms,
          article: {
            title,
            content,
            markdown,
          },
        },
      }),
    },
    120000
  );
  if (data?.error) {
    throw new Error(data.error);
  }
  return data.result;
}

export function getChannelWsPort(channel?: any): string | number | undefined {
  if (!channel?.configEncrypted) return undefined;
  try {
    return JSON.parse(channel.configEncrypted)?.wsPort || undefined;
  } catch (e) {
    return undefined;
  }
}

export function markdownToHtml(markdown: string): string {
  return String(markdown || '')
    .split(/\r?\n/)
    .map((line) => {
      const text = line.trim();
      if (!text) return '';
      if (text.startsWith('### ')) return `<h3>${escapeHtml(text.slice(4))}</h3>`;
      if (text.startsWith('## ')) return `<h2>${escapeHtml(text.slice(3))}</h2>`;
      if (text.startsWith('# ')) return `<h1>${escapeHtml(text.slice(2))}</h1>`;
      if (text.startsWith('- ')) return `<p>• ${escapeHtml(text.slice(2))}</p>`;
      return `<p>${escapeHtml(text)}</p>`;
    })
    .join('\n');
}

function escapeHtml(value: string): string {
  return value
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;');
}
