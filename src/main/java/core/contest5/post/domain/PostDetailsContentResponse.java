package core.contest5.post.domain;

import core.contest5.post.service.PostDomain;

import java.util.List;

public record PostDetailsContentResponse(
        String text,
        List<String> imageUrls,
        List<String> attachments
) {
    public static PostDetailsContentResponse from(PostDomain domain) {
        return new PostDetailsContentResponse(
                domain.getPostInfo().content().getText(),
                domain.getPostInfo().content().getImageUrls(),
                domain.getPostInfo().attachedFiles()
        );
    }
}