package cn.edu.hkustgz.subjectenvironmentranking.repository;

import cn.edu.hkustgz.subjectenvironmentranking.entity.Choice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChoiceRepository extends JpaRepository<Choice, Long> {
    @Query("SELECT DISTINCT c.leftImageId FROM Choice c UNION SELECT DISTINCT c.rightImageId FROM Choice c")
    List<String> findAllComparedImageIds();

    boolean existsByLeftImageIdAndRightImageIdOrLeftImageIdAndRightImageId(
        String leftImageId1, String rightImageId1,
        String leftImageId2, String rightImageId2
    );
}
