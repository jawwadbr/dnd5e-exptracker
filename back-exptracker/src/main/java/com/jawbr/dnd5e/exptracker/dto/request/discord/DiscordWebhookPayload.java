package com.jawbr.dnd5e.exptracker.dto.request.discord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DiscordWebhookPayload {

    private String username;
    private String avatar_url;
    private List<Embed> embeds;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class Embed {
        private String title;
        private String description;
        private int color;
        private List<Field> fields;
        private Author author;

        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        @Getter
        @Setter
        public static class Field {
            private String name;
            private String value;
            private boolean inline;

        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        @Getter
        @Setter
        public static class Author {
            private String name;
            private String icon_url;

        }
    }
}
