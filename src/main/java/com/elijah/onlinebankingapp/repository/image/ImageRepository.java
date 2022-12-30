package com.elijah.onlinebankingapp.repository.image;

import com.elijah.onlinebankingapp.model.image.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel,String> {
}
