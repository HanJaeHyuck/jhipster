package net.openobject.service;

import java.util.Optional;
import net.openobject.domain.Attachment;
import net.openobject.repository.AttachmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Attachment}.
 */
@Service
@Transactional
public class AttachmentService {

    private final Logger log = LoggerFactory.getLogger(AttachmentService.class);

    private final AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * Save a attachment.
     *
     * @param attachment the entity to save.
     * @return the persisted entity.
     */
    public Attachment save(Attachment attachment) {
        log.debug("Request to save Attachment : {}", attachment);
        return attachmentRepository.save(attachment);
    }

    /**
     * Partially update a attachment.
     *
     * @param attachment the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Attachment> partialUpdate(Attachment attachment) {
        log.debug("Request to partially update Attachment : {}", attachment);

        return attachmentRepository
            .findById(attachment.getId())
            .map(
                existingAttachment -> {
                    if (attachment.getName() != null) {
                        existingAttachment.setName(attachment.getName());
                    }

                    if (attachment.getFile() != null) {
                        existingAttachment.setFile(attachment.getFile());
                    }
                    if (attachment.getFileContentType() != null) {
                        existingAttachment.setFileContentType(attachment.getFileContentType());
                    }

                    return existingAttachment;
                }
            )
            .map(attachmentRepository::save);
    }

    /**
     * Get all the attachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Attachment> findAll(Pageable pageable) {
        log.debug("Request to get all Attachments");
        return attachmentRepository.findAll(pageable);
    }

    /**
     * Get one attachment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Attachment> findOne(Long id) {
        log.debug("Request to get Attachment : {}", id);
        return attachmentRepository.findById(id);
    }

    /**
     * Delete the attachment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Attachment : {}", id);
        attachmentRepository.deleteById(id);
    }
}
