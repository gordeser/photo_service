package org.gordeser.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.gordeser.backend.entity.Image;
import org.gordeser.backend.entity.Post;
import org.gordeser.backend.exception.EmptyFile;
import org.gordeser.backend.exception.NotFound;
import org.gordeser.backend.messages.LogMessages;
import org.gordeser.backend.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Service class for managing images.
 * <p>
 * This service provides methods for creating, retrieving, associating, and deleting images.
 * It integrates with Cloudinary for image storage and the database for managing image entities.
 * </p>
 *
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    /**
     * Repository for managing image entities in the database.
     */
    private final ImageRepository imageRepository;

    /**
     * AWS service for uploading images in AWS S3.
     */
    private final AwsService awsService;

    /**
     * Retrieves all images from the database.
     *
     * @return a list of all images
     */
    public List<Image> readAll() {
        return imageRepository.findAll();
    }

    /**
     * Retrieves an image by its ID.
     *
     * @param imageId the ID of the image to retrieve
     * @return the image with the specified ID
     * @throws NotFound if the image is not found
     */
    public Image getImageById(final Long imageId) throws NotFound {
        log.info(LogMessages.IMAGE_RETRIEVE_ATTEMPT.getMessage(), imageId);

        Image image = imageRepository.findById(imageId).orElse(null);
        if (image == null) {
            log.warn(LogMessages.IMAGE_NOT_FOUND.getMessage(), imageId);
            throw new NotFound();
        }

        log.info(LogMessages.IMAGE_RETRIEVED_SUCCESS.getMessage(), imageId);
        return image;
    }

    /**
     * Creates a new image by uploading it to AWS S3.
     *
     * @param file the file to upload and create an image from
     * @return the created {@link Image} entity
     * @throws EmptyFile if an error occurs during file upload or the file is empty
     */
    @SneakyThrows(IOException.class)
    public Image createImage(MultipartFile file) throws EmptyFile {
        if (file.isEmpty()) {
            log.error(LogMessages.FILE_EMPTY_ERROR.getMessage());
            throw new EmptyFile();
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String contentType = file.getContentType();
        Long fileSize = file.getSize();
        InputStream inputStream = file.getInputStream();

        log.info(LogMessages.IMAGE_CREATION_ATTEMPT.getMessage(), file.getOriginalFilename());
        String uploadedFileUrl = awsService.uploadFile(fileName, fileSize, contentType, inputStream);

        Image image = new Image();
        image.setFile(uploadedFileUrl);
        Image savedImage = imageRepository.save(image);
        log.info(LogMessages.IMAGE_CREATED_SUCCESS.getMessage(), savedImage.getId());

        return savedImage;
    }

    /**
     * Deletes an image by its ID.
     *
     * @param imageId the ID of the image to delete
     * @throws NotFound if the image is not found
     */
    public void deleteById(final Long imageId) throws NotFound {
        log.info(LogMessages.IMAGE_DELETE_ATTEMPT.getMessage(), imageId);

        Image imageToDelete = imageRepository.findById(imageId).orElse(null);
        if (imageToDelete == null) {
            log.warn(LogMessages.IMAGE_NOT_FOUND.getMessage(), imageId);
            throw new NotFound();
        }

        imageRepository.delete(imageToDelete);
        log.info(LogMessages.IMAGE_DELETED_SUCCESS.getMessage(), imageId);
    }

    /**
     * Associates a post with an image.
     *
     * @param image   the image to associate the post with
     * @param newPost the post to associate with the image
     */
    public void addPostToImages(final Image image, final Post newPost) {
        log.info(LogMessages.POST_ADDED_TO_IMAGE.getMessage(), newPost);
        image.setPost(newPost);
        imageRepository.save(image);
    }

    /**
     * Removes the association of a post from an image.
     *
     * @param image       the image to disassociate the post from
     * @param updatedPost the post to disassociate from the image
     */
    public void deletePostFromImage(final Image image, final Post updatedPost) {
        log.info(LogMessages.POST_REMOVED_FROM_IMAGE.getMessage(), updatedPost);
        image.setPost(null);
        imageRepository.delete(image);
    }
}
