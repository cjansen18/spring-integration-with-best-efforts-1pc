package com.chrisjansen.repository;

import com.chrisjansen.entity.MessageTrack;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @auhor:Chris Jansen
 */
public interface MessageTrackRepository extends JpaRepository<MessageTrack, Long> {
}
