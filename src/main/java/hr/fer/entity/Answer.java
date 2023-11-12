package hr.fer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "answer", schema = "public")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answerText;

    private boolean correct;

    private boolean selected;

    @JsonIgnore
    @ManyToOne
    private Question question;

    //copy constructor
    public Answer(Answer copy, Question q){
        this.id= copy.getId();
        this.answerText = copy.getAnswerText();
        this.correct = copy.isCorrect();
        this.selected = copy.isSelected();
        this.question = q;
    }
}
