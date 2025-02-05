package aist.cargo.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class CloudinaryConfig {
    private final Cloudinary cloudinary;

    public CloudinaryConfig() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dl3arz5d4",
                "api_key", "468955543427489",
                "api_secret", "V8z3Fy0MI1u1Z5DYhcKz7Me0SIQ"
        ));
    }
}
