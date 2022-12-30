package com.elijah.onlinebankingapp.model.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Data
@Entity
@NoArgsConstructor
public class ImageModel {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    private String id;
    private String fileType;
    private String fileName;

    public ImageModel(String fileType, String fileName, byte[] data) {
        this.fileType = fileType;
        this.fileName = fileName;
        this.data = data;
    }

    @Lob
    private byte[] data;
}
