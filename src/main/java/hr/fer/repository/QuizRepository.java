package hr.fer.repository;

import hr.fer.entity.Quiz;
import hr.fer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
	List<Quiz> findAllByMasterQuiz(boolean isMasterQuiz);
    List<Quiz> findAllByCreatedBy(User user);
    List<Quiz> findAllByTakenBy(User user);

    List<Quiz> findALlByMasterQuizObjectId(Long masterQuizId);

    @Query("SELECT COUNT(q) FROM Quiz q WHERE q.masterQuizObject IS NOT NULL AND q.masterQuizObject.id = :quizId")
    long countQuizzesWithMasterCopy(@Param("quizId") Long quizId);

    @Query("SELECT COUNT(q) FROM Quiz q WHERE q.masterQuizObject IS NOT NULL AND q.finished = true AND q.masterQuizObject.id = :quizId")
    long countFinishedQuizzesWithMasterCopy(@Param("quizId") Long quizId);
    
    @Query("SELECT q FROM Quiz q WHERE q.masterQuizObject IS NOT NULL AND q.finished = true AND q.masterQuizObject.id = :quizId")
    List<Quiz> getQuizzesFromMaster(@Param("quizId") Long quizId);

    @Query("SELECT q FROM Quiz q WHERE q.masterQuiz = true AND (LOWER(q.quizName) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(q.description) LIKE LOWER(CONCAT('%', :term, '%')))")
    List<Quiz> searchMasterQuizzesByNameOrDescription(@Param("term") String term);

    @Query("SELECT q FROM Quiz q WHERE q.masterQuiz = true AND q.category.id = :categoryId AND (LOWER(q.quizName) LIKE LOWER(CONCAT('%', :term, '%')) OR LOWER(q.description) LIKE LOWER(CONCAT('%', :term, '%')))")
    List<Quiz> searchMasterQuizzesWithCategoryByNameOrDescription(@Param("term") String term, @Param("category") Long categoryId);

    @Query("SELECT q FROM Quiz q WHERE q.masterQuiz = true AND q.createdBy.id = :userId")
    List<Quiz> getMasterQuizzesCreatedByUser(@Param("userId") Long userId);

}
