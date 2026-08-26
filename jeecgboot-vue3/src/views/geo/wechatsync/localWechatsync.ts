function localBridgeBase(wsPort?: string | number): string {
  const port = Number(wsPort || 9527);
  return `http://127.0.0.1:${port + 1}`;
}

export async function checkLocalWechatsync(wsPort?: string | number): Promise<{ connected: boolean }> {
  const res = await fetch(`${localBridgeBase(wsPort)}/status`, { cache: 'no-store' });
  if (!res.ok) {
    throw new Error(`本地 Wechatsync 服务返回 HTTP ${res.status}`);
  }
  return res.json();
}

export async function publishViaLocalWechatsync(
  platforms: string[],
  title: string,
  markdown: string,
  wsPort?: string | number
): Promise<{ results?: Array<{ platform: string; success: boolean; postId?: string; postUrl?: string; error?: string }>; syncId?: string }> {
  const status = await checkLocalWechatsync(wsPort);
  if (!status.connected) {
    throw new Error('Chrome 扩展未连接，请先检查扩展的同步桥接');
  }

  const content = markdownToHtml(markdown);
  const res = await fetch(`${localBridgeBase(wsPort)}/request`, {
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
  });

  let data: any = {};
  try {
    data = await res.json();
  } catch (e) {
    // keep empty body
  }
  if (!res.ok || data.error) {
    throw new Error(data.error || `本地发布失败：HTTP ${res.status}`);
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
