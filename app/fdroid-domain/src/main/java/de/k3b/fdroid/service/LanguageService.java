/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid.v1 the fdroid json catalog-format-v1 parser.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */

package de.k3b.fdroid.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.k3b.fdroid.domain.Localized;
import de.k3b.fdroid.domain.interfaces.LocaleRepository;
import de.k3b.fdroid.util.StringUtil;

/**
 * Localisation language info from org.fdroid.v1 is deliverd in a non standard way
 * resulting in different representations of the same language.
 * <p>
 * This App uses only one locale per language so that
 * en, en-AU, en-GB, en-US, en-rUS, en-us and en_US will all be mapped to "en"
 * <p>
 * Exception: chinese mainland zh-CN and taiwan zh-TW use different symbols: no canonical
 */

public class LanguageService {
    private static final Map<String, String> LANGUAGE_DEFINITIONS = defineLanguages();

    private final LocaleRepository localeRepository;

    Map<Integer, de.k3b.fdroid.domain.Locale> id2Locale = null;
    Map<String, de.k3b.fdroid.domain.Locale> code2Locale = null;

    public LanguageService(LocaleRepository localeRepository) {
        this.localeRepository = localeRepository;
    }

    public static boolean setTranslations(String localeCode, de.k3b.fdroid.domain.Locale locale) {
        // "de|German|Deutsch|symbol"
        String[] languageTranslation = getLanguageTranslation(localeCode);
        if (languageTranslation != null) {
            if (locale.getNameEnglish() == null) locale.setNameEnglish(languageTranslation[1]);
            if (locale.getNameNative() == null) locale.setNameNative(languageTranslation[2]);
            if (locale.getSymbol() == null && languageTranslation.length >= 4)
                locale.setSymbol(languageTranslation[3]);
        }
        return languageTranslation != null;
    }

    /**
     * create a canonical Locale :
     * en, en-AU, en-GB, en-US, en-rUS, en-us and en_US will all be mapped to "en"
     */
    public static String getCanonicalLocale(String fdroidLocale) {
        String normalized = fdroidLocale;
        if (normalized != null) {
            if (normalized.compareToIgnoreCase("zh") == 0) return "zh-CN";
            if (normalized.length() <= 2) {
                return normalized.toLowerCase(Locale.ROOT);
            }

            if (normalized.length() == "de-rDE".length() && normalized.contains("-r")) {
                // i.e. "de-rDE" => "de-DE"
                normalized = normalized.replace("-r", "-");
            }

            // i.e. "de_DE" => "de-DE"
            normalized = normalized.replace("_", "-");

            String normalizedLowerCase = normalized.toLowerCase(Locale.ROOT);


            if (normalized.charAt(2) == '-' && !normalizedLowerCase.startsWith("zh-")) {
                // chinese mainland zh-CN and taiwan zh-TW use different symbols: no canonical

                // indonesian: differenece between java-language and iso-language
                if (normalizedLowerCase.startsWith("in")) return "id";

                // ie "en-US" => "en"
                return normalizedLowerCase.substring(0, 2);
            }
        }

        // indonesian: differenece between java-language and iso-language
        if (normalized.startsWith("in")) return "id";

        return normalized;
    }

    /**
     * true if eigher 2 char locale or 5 char zh-xx
     */
    public static boolean isValidCanonical(String canonical) {
        int length = canonical == null ? 0 : canonical.length();
        return ((length == 2) || (length == 5 && canonical.startsWith("zh-")));
    }

    /**
     * return array of keys-offsets where canonical locale changes. keys are sorted
     */
    public static Integer[] getCanonicalLocaleChangeIndex(String[] keys) {
        Arrays.sort(keys, 0, keys.length, String.CASE_INSENSITIVE_ORDER);

        List<Integer> result = new ArrayList<>();
        String last = "";
        int i = 0;
        while (i < keys.length) {
            String canonical = LanguageService.getCanonicalLocale(keys[i]);
            if (!canonical.equals(last)) {
                result.add(i);
                last = canonical;
            }
            i++;
        }
        result.add(i);
        return result.toArray(new Integer[0]);
    }

    private static Map<String, String> defineLanguages() {
        String definitions = "ar|Arabic|العربية|\uD83C\uDDF8\uD83C\uDDE6|\n" +
                "be|Belarusian|Беларуская||\n" +
                "bg|Bulgarian|Български||\n" +
                "bn|Bengali|বাংলা||\n" +
                "ca|Catalan|Català||\n" +
                "cs|Čeština|Čeština||\n" +
                "cy|Welsh|Cymraeg||\n" +
                "da|Danish|Dansk|\uD83C\uDDE9\uD83C\uDDF0|\n" +
                "de|German|Deutsch|\uD83C\uDDE9\uD83C\uDDEA|\n" +
                "el|Greek|Ελληνικά||\n" +
                "en|English|English|\uD83C\uDDFA\uD83C\uDDF8 \uD83C\uDDEC\uD83C\uDDE7|\n" +
                "eo|Esperanto|Esperanto|\uD83D\uDDFA|\n" +
                "es|Spanish|Español|\uD83C\uDDEA\uD83C\uDDF8|\n" +
                "et|Estonian|Eesti||\n" +
                "eu|Basque|Euskara||\n" +
                "fa|Persian|فارسی|\uD83C\uDDEE\uD83C\uDDF7|\n" +
                "fi|Finland|Suomi||\n" +
                "fr|French|Français|\uD83C\uDDEB\uD83C\uDDF7|\n" +
                "gl|Galipe|Galego||\n" +
                "he|Abrit|עברית||\n" +
                "hi|Hindi|हिन्दी||\n" +
                "hr|Croatian|Hrvatski||\n" +
                "hu|Hungarian|Magya|\uD83C\uDDED\uD83C\uDDFA|\n" +
                "hy|Armenian|Հայերեն||\n" +
                "id|Indonesian|Bahasa Indonesia|\uD83C\uDDEE\uD83C\uDDE9|\n" +
                "it|Italian|Italiano|\uD83C\uDDEE\uD83C\uDDF9|\n" +
                "ja|Japanese|日本語|\uD83C\uDDEF\uD83C\uDDF5|\n" +
                "ka|Georgian|ქართული||\n" +
                "ko|korean|한국어||\n" +
                "la|Latina|Latina||\n" +
                "lt|Lithuanian|Lietuvių||\n" +
                "lv|Latvian|Latviešu||\n" +
                "mk|Macedonian|Македонски||\n" +
                "ms|Malay|Bahasa Melayu||\n" +
                "my|Myanmarian|မြန်မာဘာသာ||\n" +
                "nl|Dutch|Nederlands|\uD83C\uDDF3\uD83C\uDDF1|\n" +
                "no|Norwegian|Norsk (bokmål)|\uD83C\uDDF3\uD83C\uDDF4|\n" +
                "pl|Polish|Polski|\uD83C\uDDF5\uD83C\uDDF1|\n" +
                "ps|Pashto|پښتو||\n" +
                "pt|Portuguese|Português|\uD83C\uDDF5\uD83C\uDDF9 \uD83C\uDDE7\uD83C\uDDF7|\n" +
                "ro|Romanian|Română|\uD83C\uDDF7\uD83C\uDDF4|\n" +
                "ru|Russian|Русский|\uD83C\uDDF7\uD83C\uDDFA|\n" +
                "sh|Serbo-Croatian|Srpskohrvatski / Српскохрватски||\n" +
                "sk|Slovak|Slovenčina|\uD83C\uDDF8\uD83C\uDDEE||\n" +
                "sl|Slovene|Slovenščina|\uD83C\uDDF8\uD83C\uDDF0||\n" +
                "sr|Serbian|Српски / Srpski||\n" +
                "sv|Swedish|Svenska|\uD83C\uDDF8\uD83C\uDDEA|\n" +
                "ta|Tamil|தமிழ்||\n" +
                "tg|Tajik|Тоҷикӣ||\n" +
                "th|Thai|ภาษาไทย||\n" +
                "tr|Turkish|Türkçe|\uD83C\uDDF9\uD83C\uDDF7|\n" +
                "uk|Ukrainian|Українська|\uD83C\uDDFA\uD83C\uDDE6|\n" +
                "ur|Urdu|اردو||\n" +
                "uz|Uzbek|Oʻzbekcha / Ўзбекча||\n" +
                "vi|Vietnamese|Tiếng Việt|\uD83C\uDDFB\uD83C\uDDF3|\n" +
                "zh-CN|Simplified Chinese|简体中文||\n" +
                "zh-TW|Traditional Chinese / Taiwanese|繁体中文||\ngd|Scottish Gaelic|Gaelic\n" +
                "sq|Albanian|Albanian\n" +
                "nn|Norwegian|Nynorsk\n" +
                "is|Icelandic|Icelandic\n" +
                "af|Afrikaans|Afrikaans\n" +
                "sw|Swahili|Swahili\n" +
                "am|Amharic|Amharic\n" +
                "sc|Sardinian|Sardinian\n" +
                "bo|Tibetan|Tibetan\n" +
                "az|Azerbaijani|Azerbaijani\n" +
                "si|Sinhala / Sinhalese|Sinhala / Sinhalese\n" +
                "ml|Malayalam|Malayalam\n" +
                "zh-HK|Chinese Honkong|Chinese Honkong\n" +
                "mr|Marathi|Marathi\n" +
                "nb|Norwegian Bokmål|Bokmål\n";
        Map<String, String> result = new HashMap<>();
        for (String line : definitions.split("\n")) {
            result.put(line.split("\\|")[0], line);
        }
        return result;
    }

    /**
     * "de|German|Deutsch|symbol" +
     */
    public static String[] getLanguageTranslation(String languageCode) {
        String line = LANGUAGE_DEFINITIONS.get(languageCode);
        if (line == null) return null;
        return line.split("\\|");
    }

    public void init() {
        List<de.k3b.fdroid.domain.Locale> locales = localeRepository.findAll();
        id2Locale = new HashMap<>();
        code2Locale = new HashMap<>();

        for (de.k3b.fdroid.domain.Locale locale : locales) {
            init(locale);
        }
    }

    private void init(de.k3b.fdroid.domain.Locale locale) {
        id2Locale.put(locale.getId(), locale);
        code2Locale.put(locale.getCode(), locale);
    }

    public String getLocaleCode(int localeId) {
        de.k3b.fdroid.domain.Locale locale = (localeId == 0) ? null : id2Locale.get(localeId);
        return (locale == null) ? null : locale.getCode();
    }

    public int getOrCreateLocaleId(String localeCode) {
        if (localeCode != null) {
            de.k3b.fdroid.domain.Locale locale = code2Locale.get(localeCode);
            if (locale == null) {
                // create on demand
                locale = new de.k3b.fdroid.domain.Locale();
                locale.setCode(localeCode);

                setTranslations(localeCode, locale);
                localeRepository.insert(locale);
                init(locale);
            } else if (StringUtil.isEmpty(locale.getNameEnglish())) {
                if (setTranslations(localeCode, locale)) {
                    localeRepository.update(locale);
                }
            }
            return locale.getId();
        }
        return 0;
    }

    public Localized findByLocaleId(List<Localized> roomLocalizedList, int localeId) {
        for (Localized l : roomLocalizedList) {
            if (l.getLocaleId() == localeId) return l;
        }
        return null;
    }
}
