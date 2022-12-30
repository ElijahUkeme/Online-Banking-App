package com.elijah.onlinebankingapp.response.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {

    private String fileName;
    private String downloadUrl;
    private String fileType;
    private long fileSize;
}
