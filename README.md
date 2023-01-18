# HELPDESK

autoři semestrální práce: Adam Roth, Aneta Drahoňovská

## Bližší popis

Vytvoření zákaznického servisu, který svým uživatelům poskytne prostor pro přímou komunikaci se členy podpory. Zákazníci
tak mohou podávat veškeré dotazy, návrhy a další náměty prostřednictvím ticketů na jednom přehledném místě a to s
minimální reakční dobou zaměstnanců, která je uzpůsobena mimo jiné díky možnosti určení úrovně závažnosti (kompetence
vedoucího společnosti) a typu zákaznického podnětu. Dále mohou uživatelé reagovat prostřednictvím komentářů na odpovědi
zaměstnanců podpory. Vedoucí přiděluje své pracovníky k jednotlivým ticketům a také určuje termíny do jejich nutného
splnění. Zaměstnanci mají možnost sledovat výši času stráveného nad daným problémem a jsou nuceni informovat zákazníka o
postupu řešení. Jestliže zákazník projeví svou spokojenost s takovýmto řešením zaměstnance, vedoucí tuto záležitost
uzavře.

## Komu je systém určen

Náš projekt je určen především uživatelům jakožto zákaznický servis, jehož cílem je zkvalitnit komunikaci mezi
zaměstnanci podpory a uživateli.Systém však zároveň napomůže také samotným zaměstnancům a to zefektivněním organizace
práce skrze přidělování k ticketům, rozlišení a měření odpracovaného času nad řešením ticketů.

## Poskytované funkcionality

### Zákazník

- Vytváření nových ticketů
- Přidávání zpráv do ticketů
- Uzavírání vlastních ticketů
- Zobrazení přehledu všech svých ticketů
- Možnost vyjádření (ne)spokojenosti s prací pracovníka podpory
  (po uzavření ticketu pracovníkem)

### Podpora

****1. Běžný pracovník****

- Měření času stráveného nad řešením
- Odepisování na zákaznické tickety, kam je pracovník přiřazen
- Uzavření ticketu zákazníka

****2. Vedoucí pracovník****

- Určení závažnosti ticketu zákazníka
- Uzavření jakéhokoli ticketu
- Přiřazení pracovníka podpory k danému ticketu

## Co systém neposkytuje

- integraci s ostatními systémy

## Class diagram

![Class Diagram](https://lh3.googleusercontent.com/u/3/drive-viewer/AFDK6gPEvi2rJrLP-dlorADI2R38ejKjtFdTugojMZ6fhvJtfc7Qzh0JCC_I9xneEIon1EKYKuuwNc1_wrHoB5Row9r6m5hhIw=w3360-h1762) \
Pokud se obrázek nenačte, klikněte [zde](https://lh3.googleusercontent.com/u/3/drive-viewer/AFDK6gPEvi2rJrLP-dlorADI2R38ejKjtFdTugojMZ6fhvJtfc7Qzh0JCC_I9xneEIon1EKYKuuwNc1_wrHoB5Row9r6m5hhIw=w3360-h1762).

---

# Závěrečná zpráva o projektu
Helpdesk – Roth Adam, Drahoňovská Aneta

## Popis aplikace
Aplikace představuje zákaznický servis, který svým uživatelům poskytuje prostor pro přímou komunikaci se členy podpory. Zákazníci tak mohou podávat veškeré dotazy, návrhy a další náměty prostřednictvím ticketů na jednom přehledném místě a to s minimální reakční dobou zaměstnanců, která je uzpůsobena mimo jiné díky možnosti určení úrovně závažnosti (kompetence vedoucího společnosti) a typu zákaznického podnětu. Dále mohou uživatelé reagovat prostřednictvím komentářů na odpovědi zaměstnanců podpory. Vedoucí může přidělit své pracovníky k jednotlivým ticketům. Zaměstnanci mají možnost sledovat výši času stráveného nad daným problémem a jsou nuceni informovat zákazníka o postupu řešení. Jestliže zákazník projeví svou spokojenost s takovýmto řešením zaměstnance, vedoucí tuto záležitost uzavře.

## Struktury aplikace
Aplikace sestává z REST controllerů, které volají servisní vrstvu, jež dále modifikuje data poskytované skrze repozitáře jednotlivých entit. Controllery na základě HTTP dotazu uživatele na daném endpointu přemapují získáná data od nižších vrstev do DTO struktur a vrací je uživateli na výstup. Controllery jsou zabezpečené pomocí technologie Spring Security, která na základě nejen role přihlášeného uživatele (manager/employee/customer) určuje jeho možnosti přístupu k daným funkcionalitám aplikace.  

## Návod pro spuštění (IDE)
1. Otevřte projekt v IDE (ideálně v JetBrains Idea)
2. Spusťte aplikaci pomocí IDE
3. Po spuštění aplikace můžete použít endpointy nacházející se na adrese http://localhost:8080/swagger-ui/index.html

## Návod pro spuštění (JAR)
1. Otevřte kořenovou složku projektu
2. Zadejte do terminálu příkaz: `mvn clean install`
3. Ve složce `./target` se Vám vygeneruje JAR soubor s názvem `helpdesk-0.0.1-SNAPSHOT.jar`, který je možné přesunout do složky dle libosti.
4. Ve stejné složce jako je zmiňovaný soubor zadejte do terminálu příkaz: `java -jar helpdesk-0.0.1-SNAPSHOT.jar`
3. Po spuštění aplikace můžete použít endpointy nacházející se na adrese http://localhost:8080/swagger-ui/index.html

### Přihlašovací údaje
(s basic authentication)
- manager: username - peter@tee.com, heslo - none
- employee: username - john@smith.com, heslo - none 
- customer: username - alan@black.com, heslo - none 

## Získané zkušenosti
Práce na semestrálním projektu byla naší první zkušeností s takto komplexní aplikací a celkově Springem. Byli jsme tak na začátku zděšeni množstvím požadavků na práci, nicméně po sestavení základní kostry aplikace a ujasnění si co musíme udělat, byla práce na rozdíl od očekávání vcelku intuitivní a začala jsem si tak její rozsáhlosti za cenu jasného rozlišení odpovědností a probublávání struktur cenit.

