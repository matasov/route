package task.homework.route.web.models;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CountryDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Language {
        private String official;
        private String common;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Name {
        private String common;
        private String official;
        @JsonProperty("native")
        private Map<String, Language> nativeValue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Currency {
        private String name;
        private String symbol;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Idd {
        private String root;
        private List<String> suffixes;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Translation {
        private String official;
        private String common;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Demonyms {
        private String f;
        private String m;
    }

    @NoArgsConstructor
    @Data
    public class GeoPoint {
        private Double latitude;
        private Double longitude;
    }

    private Name name;
    private List<String> tld;
    private String cca2;
    private String ccn3;
    private String cca3;
    private String cioc;
    private boolean independent;
    private String status;
    private boolean unMember;
    private Map<String, Currency> currencies;
    private Idd idd;
    private List<String> capital;
    private List<String> altSpellings;
    private String region;
    private String subregion;
    private Map<String, String> languages;
    private Map<String, Translation> translations;
    private List<Double> latlng;
    private boolean landlocked;
    private List<String> borders;
    private int area;
    private String flag;
    private Map<String, Demonyms> demonyms;

}
