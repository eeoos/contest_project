package core.contest5.post.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostContent {
    @Column(columnDefinition = "TEXT")
    private String text;

    @ElementCollection
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    public void addImageUrl(String imageUrl) {
        this.imageUrls.add(imageUrl);
    }

    public void removeImageUrl(String imageUrl) {
        this.imageUrls.remove(imageUrl);
    }
}