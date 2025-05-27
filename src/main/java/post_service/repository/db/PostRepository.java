package post_service.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import post_service.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
