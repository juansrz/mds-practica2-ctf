import requests
import re
from bs4 import BeautifulSoup
from urllib.parse import urljoin, urlparse

# Variables globales para acumular los conteos y llevar registro de URLs visitadas
visited_urls = set()
total_full_count = 0
total_article_count = 0
pattern = re.compile(r'\bURJC\b')

# Dominio permitido para restringir el crawling
allowed_domain = "r2-ctf-vulnerable.numa.host"

def is_allowed(url):
    parsed = urlparse(url)
    return allowed_domain in parsed.netloc

def dfs_crawl(url):
    global total_full_count, total_article_count, visited_urls, pattern

    # Evitar ciclos: si la URL ya fue visitada, se omite
    if url in visited_urls:
        return
    visited_urls.add(url)

    try:
        response = requests.get(url, timeout=10)
    except Exception as e:
        print(f"Error al acceder a {url}: {e}")
        return

    html = response.text

    # Parsear el HTML para extraer los <div class="article-post">
    soup = BeautifulSoup(html, 'html.parser')
    article_divs = soup.find_all("div", class_="article-post")
    article_html = " ".join(str(div) for div in article_divs)
    article_count = len(pattern.findall(article_html))
    total_article_count += article_count

    print(f"URL: {url}")
    print(f"  Apariciones en div.article-post: {article_count}\n")

    # Extraer y recorrer todos los enlaces de la página
    links = soup.find_all("a", href=True)
    for link in links:
        next_url = urljoin(url, link['href'])
        if is_allowed(next_url):
            dfs_crawl(next_url)

def main():
    start_url = "https://r2-ctf-vulnerable.numa.host/"
    dfs_crawl(start_url)
    print("Crawling completado.\n")
    print(f"Total de apariciones en div.article-post: {total_article_count}")

main()
