package post_service.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import post_service.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
}