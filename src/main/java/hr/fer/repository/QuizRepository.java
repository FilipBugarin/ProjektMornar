package hr.fer.repository;

import hr.fer.entity.Quiz;
import hr.fer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    List<Quiz> searchMasterQuizzesWithCategoryByNameOrDescription(@Param("term") String term, @Param("categoryId") Long categoryId);

    @Query("SELECT q FROM Quiz q WHERE q.masterQuiz = true AND q.createdBy.id = :userId")
    List<Quiz> getMasterQuizzesCreatedByUser(@Param("userId") Long userId);

    @Query("SELECT q.masterQuizObject.id, COUNT(*) FROM Quiz q WHERE q.masterQuiz = false AND q.finished = true GROUP BY q.masterQuizObject.id")
    List<Object[]> getMasterQuizzesWithCount();

    @Query("SELECT q FROM Quiz q WHERE q.masterQuiz = false AND q.finished = true AND q.takenBy.id = :userId AND (q.masterQuizObject.id, q.finishedTime) IN (SELECT q1.masterQuizObject.id, MAX(q1.finishedTime) FROM Quiz q1 WHERE q1.masterQuiz = false AND q1.finished = true AND q.takenBy.id = :userId GROUP BY q1.masterQuizObject.id)")
    List<Quiz> getLastThreeMasterQuizzes(@Param("userId") Long userId);

}
