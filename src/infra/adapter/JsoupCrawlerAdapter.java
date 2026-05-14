package infra.adapter;

import domain.port.CrawlerPort;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Adapter que implementa CrawlerPort usando Jsoup para web scraping.
 * Extrai o preço de páginas de e-commerce (VTEX e outros).
 */
public class JsoupCrawlerAdapter implements CrawlerPort {

    private static final int TIMEOUT_MS = 15000;

    @Override
    public Float buscarPreco(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(TIMEOUT_MS)
                    .get();

            // Estratégia 1: JSON-LD (structured data — comum em VTEX)
            Float preco = extrairDeJsonLd(doc);
            if (preco != null) return preco;

            // Estratégia 2: Meta tags (product:price:amount, og:price:amount)
            preco = extrairDeMetaTag(doc);
            if (preco != null) return preco;

            // Estratégia 3: Seletores CSS comuns
            preco = extrairDeSeletorCss(doc);
            if (preco != null) return preco;

            System.out.println("[Crawler] Não foi possível extrair o preço da URL: " + url);
            return null;

        } catch (Exception e) {
            System.out.println("[Crawler] Erro ao acessar URL: " + url);
            System.out.println("[Crawler] Detalhe: " + e.getMessage());
            return null;
        }
    }

    private Float extrairDeJsonLd(Document doc) {
        Elements scripts = doc.select("script[type=application/ld+json]");
        for (Element script : scripts) {
            String json = script.html();
            int priceIndex = json.indexOf("\"price\"");
            if (priceIndex == -1) {
                priceIndex = json.indexOf("\"Price\"");
            }
            if (priceIndex != -1) {
                return extrairNumeroApos(json, priceIndex);
            }
        }
        return null;
    }

    private Float extrairDeMetaTag(Document doc) {
        Element meta = doc.selectFirst("meta[property=product:price:amount]");
        if (meta != null) {
            return parsePreco(meta.attr("content"));
        }
        meta = doc.selectFirst("meta[property=og:price:amount]");
        if (meta != null) {
            return parsePreco(meta.attr("content"));
        }
        return null;
    }

    private Float extrairDeSeletorCss(Document doc) {
        String[] seletores = {
                "[class*=sellingPrice]",
                "[class*=bestPrice]",
                "[class*=price] [class*=value]",
                ".product-price .best-price",
                ".price"
        };
        for (String seletor : seletores) {
            Element el = doc.selectFirst(seletor);
            if (el != null) {
                Float preco = parsePreco(el.text());
                if (preco != null) return preco;
            }
        }
        return null;
    }

    private Float extrairNumeroApos(String json, int startIndex) {
        int colonIndex = json.indexOf(':', startIndex);
        if (colonIndex == -1) return null;

        StringBuilder sb = new StringBuilder();
        boolean encontrouDigito = false;
        for (int i = colonIndex + 1; i < json.length() && i < colonIndex + 30; i++) {
            char c = json.charAt(i);
            if (Character.isDigit(c) || c == '.') {
                sb.append(c);
                encontrouDigito = true;
            } else if (encontrouDigito) {
                break;
            }
        }

        if (sb.length() > 0) {
            try {
                return Float.parseFloat(sb.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private Float parsePreco(String texto) {
        if (texto == null || texto.isBlank()) return null;
        try {
            String limpo = texto
                    .replaceAll("[R$\\s]", "")
                    .replace(".", "")
                    .replace(",", ".");
            return Float.parseFloat(limpo);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
