package cn.edu.hkustgz.subjectenvironmentranking.repository;

import cn.edu.hkustgz.subjectenvironmentranking.entity.ImageComparison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageComparisonRepository extends JpaRepository<ImageComparison, Long> {

    @Query("SELECT DISTINCT ic.leftImageId FROM ImageComparison ic UNION SELECT DISTINCT ic.rightImageId FROM ImageComparison ic")
    List<String> findAllComparedImageIds();

    @Query("SELECT CASE WHEN COUNT(ic) > 0 THEN true ELSE false END FROM ImageComparison ic " +
           "WHERE (ic.leftImageId = ?1 AND ic.rightImageId = ?2) OR (ic.leftImageId = ?2 AND ic.rightImageId = ?1)")
    boolean existsByImagePair(String imageId1, String imageId2);
}
