package hr.fer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "question", schema = "public")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionString;
    private boolean randomOrder;

    private int position;

    @JsonIgnore
    @ManyToOne
    private Quiz quiz;

    @JsonIgnore
    @ManyToOne
    private Question masterQuestion;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<Answer> answerList;

    public Question(Question that){
        this(that.getId(),that.getQuestionString(),that.isRandomOrder(), that.position, that.getQuiz(), that.getMasterQuestion() ,that.getAnswerList());
    }
}
