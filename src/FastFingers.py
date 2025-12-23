from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time


driver = webdriver.Chrome()
driver.get("http://localhost:63343/10fastfingers/index.html?_ijt=msim7e1ao84c59oc7ea5h1tgk8")
time.sleep(2)

words_element = driver.find_element(By.CLASS_NAME, "text-display")
words = words_element.text.split()

input_box = WebDriverWait(driver, 10).until(
    EC.element_to_be_clickable((By.ID, "textInput"))
)
input_box.click()

for w in words:
    input_box.send_keys(w + " ")
    time.sleep(0.05)

time.sleep(30000)
driver.quit()
