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
    private int position;

    @JsonIgnore
    @ManyToOne
    private Question question;

    public Answer(Answer that){
        this(that.getId(), that.getAnswerText(), that.isCorrect(), that.isSelected(), that.position, that.getQuestion());
    }
}
