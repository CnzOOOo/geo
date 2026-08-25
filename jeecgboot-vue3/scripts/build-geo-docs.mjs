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
  'GEO-MENU-GUIDE.md',
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

const publicFiles = ['GEO-MENU-GUIDE.md'];
const files = publicFiles
  .filter((file) => fs.existsSync(path.join(sourceDir, file)))
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
    color-scheme: light;
    font-family: Inter, ui-sans-serif, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", "Microsoft YaHei", sans-serif;
    color: #172033;
    background: #f7f9fc;
    font-synthesis: none;
  }
  * { box-sizing: border-box; }
  html { scroll-behavior: smooth; }
  body { margin: 0; min-width: 320px; background: #f7f9fc; line-height: 1.7; }
  a { color: #1255a6; text-decoration-thickness: 1px; text-underline-offset: 3px; }
  button, a { -webkit-tap-highlight-color: transparent; }

  .topbar {
    position: sticky;
    top: 0;
    z-index: 30;
    display: flex;
    min-height: 58px;
    align-items: center;
    justify-content: space-between;
    gap: 1rem;
    border-bottom: 1px solid #dce3ec;
    background: rgba(255, 255, 255, .96);
    padding: .65rem clamp(1rem, 3vw, 2.5rem);
    backdrop-filter: blur(10px);
  }
  .brand { display: inline-flex; align-items: center; gap: .65rem; color: #172033; font-weight: 750; text-decoration: none; }
  .brand-mark { display: grid; width: 30px; height: 30px; place-items: center; border-radius: 7px; background: #1659b7; color: #fff; font-size: .85rem; }
  .top-actions { display: flex; align-items: center; gap: .65rem; flex-wrap: wrap; }
  .api-link { font-size: .82rem; font-weight: 650; }

  .docs-shell {
    display: grid;
    grid-template-columns: minmax(180px, 230px) minmax(0, 900px);
    gap: clamp(1.25rem, 3vw, 3rem);
    width: min(1200px, 100%);
    margin: 0 auto;
    padding: 2rem clamp(1rem, 3vw, 2.5rem) 5rem;
  }
  .task-nav {
    position: sticky;
    top: 82px;
    align-self: start;
    max-height: calc(100vh - 100px);
    overflow: auto;
    display: grid;
    gap: .18rem;
  }
  .task-nav a {
    border-left: 2px solid transparent;
    color: #5d6878;
    padding: .42rem .55rem;
    font-size: .82rem;
    text-decoration: none;
  }
  .task-nav a:hover { border-left-color: #1c75bc; background: #edf5fb; color: #164f84; }
  .task-nav a.active { border-left-color: #1659b7; background: #eaf2fb; color: #1255a6; font-weight: 700; }

  main { min-width: 0; }
  .doc { max-width: 900px; }
  .doc h1 { margin: 0 0 1.2rem; color: #111827; font-size: clamp(2rem, 5vw, 2.8rem); line-height: 1.15; }
  .doc h2 { margin: 2rem 0 .8rem; color: #142033; font-size: 1.45rem; scroll-margin-top: 82px; }
  .doc p, .doc li { color: #4e596b; line-height: 1.72; }
  .doc li + li { margin-top: .35rem; }
  .doc section { padding: 2.35rem 0; border-bottom: 1px solid #dce3ec; }
  .doc code { border-radius: 4px; background: #edf1f6; padding: .1rem .3rem; color: #9b2f4a; font-family: "Cascadia Code", Consolas, monospace; font-size: .88em; }
  .doc pre { margin: 1rem 0; overflow: auto; border-radius: 7px; background: #101827; padding: 1rem; color: #d9e4f2; font-size: .78rem; line-height: 1.65; }
  .doc pre code { padding: 0; background: transparent; color: inherit; }
  .doc table { width: 100%; border-collapse: collapse; font-size: .82rem; }
  .doc th, .doc td { border-bottom: 1px solid #dce3ec; padding: .7rem .55rem; text-align: left; vertical-align: top; }
  .doc th { color: #253147; background: #eef2f7; }
  .doc td { color: #566173; }
  .doc blockquote { margin: 1rem 0; border-left: 3px solid #0b7d74; background: #eaf7f5; padding: .9rem 1rem; color: #3e5f5b; }
  .doc img { max-width: 100%; }

  @media (max-width: 860px) {
    .docs-shell { display: block; padding: 1.3rem 1rem 4rem; }
    .task-nav { position: static; max-height: none; border-bottom: 1px solid #dce3ec; padding-bottom: 1rem; margin-bottom: 1rem; }
    .doc h1 { font-size: 2rem; }
    .doc table { display: block; overflow-x: auto; white-space: nowrap; }
  }

  @media print {
    .topbar, .task-nav { display: none !important; }
    .docs-shell { display: block; padding: 0; }
    body { background: #fff; }
  }
`;

function page(title, activeSlug, content, index = false) {
  const guide = items.find((item) => item.slug === 'GEO-MENU-GUIDE');
  const indexContent = index && guide ? guide.html : content;
  const nav = items
    .map((item) => {
      const active = activeSlug === item.slug ? ' class="active"' : '';
      return `<a${active} href="./${item.slug}.html">${escapeHtml(item.title)}</a>`;
    })
    .join('\n');

  return `<!doctype html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${escapeHtml(title)} - GEO 使用文档</title>
  <style>${style}</style>
</head>
<body>
  <header class="topbar">
    <a class="brand" href="./index.html">
      <span class="brand-mark" aria-hidden="true">G</span>
      <span>GEO 文档</span>
    </a>
    <div class="top-actions">
      <a class="api-link" href="https://geo.rucode.cn/jeecg-boot">GEO 后台</a>
      <a class="api-link" href="https://front.rucode.cn/">front.rucode.cn</a>
    </div>
  </header>
  <div class="docs-shell">
    <aside class="task-nav" aria-label="Documentation navigation">
      ${nav}
    </aside>
    <main id="docs-main" tabindex="-1">
      <article class="doc">${indexContent}</article>
    </main>
  </div>
</body>
</html>`;
}

for (const item of items) {
  fs.writeFileSync(path.join(outputDir, `${item.slug}.html`), page(item.title, item.slug, item.html));
  fs.copyFileSync(path.join(sourceDir, item.file), path.join(markdownDir, item.file));
}

fs.writeFileSync(path.join(outputDir, 'index.html'), page('GEO 菜单使用说明', 'GEO-MENU-GUIDE', '', true));

const appIndex = path.join(process.cwd(), 'dist', 'index.html');
if (fs.existsSync(appIndex)) {
  fs.copyFileSync(appIndex, path.join(process.cwd(), 'dist', '404.html'));
}

console.log(`GEO docs generated: ${items.length} pages -> ${outputDir}`);
