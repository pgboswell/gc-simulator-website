# GC Simulator website

The complete GC Simulator website, including the browser simulator, About and Development pages, source downloads, and contact service.

The standalone application is in [gc-simulator](https://github.com/pgboswell/gc-simulator).

## Run locally

```
python -m http.server 8000 --directory public
```

Open http://localhost:8000/. The simulator requires HTTP rather than a file URL. The simple server provides static pages; contact delivery requires the server-side service.

## Build and test

- `npm test`: numerical, plotting, website, redirect, and mocked contact checks.
- `node scripts/preview.mjs`: regenerate the homepage plot.
- `python build_site.py`: regenerate pages and source archives.

Page content is in `build_site.py`, the simulator template is in `simulator/workspace.html`, and application code is in `public/assets/simulator/`. Java reference source and extraction utilities support numerical regression; normal builds and tests use the included fixtures and need only Python and Node.js.

## Deployment

Publish `public/` on a static host, or use the included Cloudflare Worker (`worker.js`, `wrangler.jsonc`) for redirects and `/api/contact`. Canonical URLs use https://gcsimulator.org. Creating this repository does not deploy the site.

To enable contact delivery, configure server-side values: `CONTACT_TO`, `CONTACT_FROM`, `MAILGUN_DOMAIN`, `MAILGUN_API_KEY`, `TURNSTILE_SITE_KEY`, and `TURNSTILE_SECRET_KEY`. Optional `MAILGUN_REGION` is `US` or `EU`. Register the GC hostname in Turnstile. Keep all credentials out of source control and public files. The form reports unavailable when the service is not configured.

## License and attribution

Model and compound data by Paul Boswell. CC BY-NC-SA 3.0 US; see LICENSE and NOTICE. The model's assumptions and verification are documented in [MODEL.md](public/MODEL.md).
