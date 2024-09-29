package core.contest5.post.domain;

import core.contest5.post.service.PostDomain;

public record ApplicationResponse(
        String applicationDetail
) {
    public static ApplicationResponse from(PostDomain domain) {
        return new ApplicationResponse(
                switch (domain.getPostInfo().applicationMethod()) {
                    case EMAIL -> domain.getPostInfo().applicationEmail();
                    case HOMEPAGE -> domain.getPostInfo().hostHomepageURL();
                }
        );
    }
}
