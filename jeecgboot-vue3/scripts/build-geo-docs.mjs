import fs from 'node:fs';
import path from 'node:path';
import { createRequire } from 'node:module';

const require = createRequire(import.meta.url);
const MarkdownIt = require('markdown-it');

const rootDir = path.resolve(process.cwd(), '..');
const sourceDir = path.join(rootDir, 'docs', 'geo-merchant-search-rank');
const outputDir = path.join(process.cwd(), 'dist', 'docs');
const markdownDir = path.join(outputDir, 'markdown');
const md = new MarkdownIt({ html: true, linkify: true, typographer: true });

const order = [
  'README.md',
  'MERCHANT-DATA-CHECKLIST.md',
  'DEPLOYMENT-GUIDE.md',
  'INTEGRATION-CHECKLIST.md',
  'JEEG-BOOT-TASKS.md',
  'EXECUTION-30D.md',
];

function escapeHtml(value) {
  return String(value)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#39;');
}

function firstHeading(content) {
  const match = content.match(/^#\s+(.+)$/m);
  return match ? match[1].trim() : '';
}

function firstParagraph(content) {
  const lines = content
    .split(/\r?\n/)
    .map((line) => line.trim())
    .filter((line) => line && !line.startsWith('#'));
  return lines[0] || '';
}

const files = fs
  .readdirSync(sourceDir)
  .filter((file) => file.toLowerCase().endsWith('.md'))
  .sort((a, b) => {
    const ai = order.indexOf(a);
    const bi = order.indexOf(b);
    return (ai === -1 ? 99 : ai) - (bi === -1 ? 99 : bi) || a.localeCompare(b);
  });

const items = files.map((file) => {
  const raw = fs.readFileSync(path.join(sourceDir, file), 'utf8');
  const slug = file.replace(/\.md$/i, '');
  return {
    file,
    slug,
    title: firstHeading(raw) || slug,
    description: firstParagraph(raw),
    html: md.render(raw),
  };
});

fs.mkdirSync(outputDir, { recursive: true });
fs.mkdirSync(markdownDir, { recursive: true });

const style = `
  :root {
    --bg: #f6f8fa;
    --panel: #ffffff;
    --text: #1f2328;
    --muted: #57606a;
    --border: #d8dee4;
    --accent: #0969da;
  }
  * { box-sizing: border-box; }
  body {
    margin: 0;
    color: var(--text);
    background: var(--bg);
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "Microsoft YaHei", sans-serif;
    line-height: 1.7;
  }
  .layout { display: flex; min-height: 100vh; }
  .sidebar {
    width: 280px;
    flex: 0 0 280px;
    background: var(--panel);
    border-right: 1px solid var(--border);
    padding: 24px 16px;
    position: sticky;
    top: 0;
    height: 100vh;
    overflow-y: auto;
  }
  .brand { font-size: 18px; font-weight: 700; margin-bottom: 16px; }
  .brand small { display: block; color: var(--muted); font-weight: 400; font-size: 13px; }
  nav { display: grid; gap: 4px; }
  nav a {
    display: block;
    padding: 8px 10px;
    border-radius: 6px;
    color: var(--text);
    text-decoration: none;
    font-size: 14px;
  }
  nav a:hover { background: #f0f3f6; }
  nav a.active { background: #ddf4ff; color: var(--accent); font-weight: 600; }
  .content {
    flex: 1;
    min-width: 0;
    padding: 40px;
  }
  .article {
    max-width: 960px;
    margin: 0 auto;
    background: var(--panel);
    border: 1px solid var(--border);
    border-radius: 8px;
    padding: 32px 40px;
  }
  .article h1 { margin-top: 0; border-bottom: 1px solid var(--border); padding-bottom: 16px; }
  .article h2 { margin-top: 28px; border-bottom: 1px solid var(--border); padding-bottom: 8px; }
  .article table { border-collapse: collapse; width: 100%; margin: 16px 0; }
  .article th, .article td { border: 1px solid var(--border); padding: 8px 10px; text-align: left; font-size: 14px; }
  .article th { background: #f0f3f6; }
  .article pre { background: #f6f8fa; border: 1px solid var(--border); border-radius: 6px; padding: 14px; overflow-x: auto; }
  .article code { font-family: ui-monospace, SFMono-Regular, Consolas, monospace; font-size: 0.9em; }
  .article img { max-width: 100%; }
  .index-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 16px; margin-top: 24px; }
  .doc-card {
    background: var(--panel);
    border: 1px solid var(--border);
    border-radius: 8px;
    padding: 20px;
    text-decoration: none;
    color: var(--text);
  }
  .doc-card:hover { border-color: var(--accent); }
  .doc-card h2 { margin: 0 0 8px; font-size: 17px; }
  .doc-card p { color: var(--muted); margin: 0; font-size: 14px; }
  @media (max-width: 860px) {
    .layout { display: block; }
    .sidebar { width: 100%; height: auto; position: static; border-right: 0; border-bottom: 1px solid var(--border); }
    .content { padding: 16px; }
    .article { padding: 20px; }
  }
`;

function page(title, activeSlug, content, index = false) {
  const nav = items
    .map((item) => {
      const active = activeSlug === item.slug ? ' class="active"' : '';
      return `<a${active} href="./${item.slug}.html">${escapeHtml(item.title)}</a>`;
    })
    .join('\n');
  const main = index
    ? `<main class="content"><div class="article"><h1>GEO 使用文档</h1><p>JeecgBoot GEO 运营系统的使用、数据清单、部署和测试说明。</p><div class="index-grid">${items
        .map(
          (item) =>
            `<a class="doc-card" href="./${item.slug}.html"><h2>${escapeHtml(item.title)}</h2><p>${escapeHtml(item.description || '点击阅读')}</p></a>`
        )
        .join('')}</div></div></main>`
    : `<main class="content"><div class="article">${content}</div></main>`;

  return `<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${escapeHtml(title)} - GEO 使用文档</title>
  <style>${style}</style>
</head>
<body>
  <div class="layout">
    <aside class="sidebar">
      <div class="brand">GEO 使用文档<small>JeecgBoot GEO 运营系统</small></div>
      <nav>${nav}</nav>
    </aside>
    ${main}
  </div>
</body>
</html>`;
}

for (const item of items) {
  fs.writeFileSync(path.join(outputDir, `${item.slug}.html`), page(item.title, item.slug, item.html));
  fs.copyFileSync(path.join(sourceDir, item.file), path.join(markdownDir, item.file));
}

fs.writeFileSync(path.join(outputDir, 'index.html'), page('文档首页', '', '', true));

const appIndex = path.join(process.cwd(), 'dist', 'index.html');
if (fs.existsSync(appIndex)) {
  fs.copyFileSync(appIndex, path.join(process.cwd(), 'dist', '404.html'));
}

console.log(`GEO docs generated: ${items.length} pages -> ${outputDir}`);
