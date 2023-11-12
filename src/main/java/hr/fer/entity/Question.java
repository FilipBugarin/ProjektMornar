package hr.fer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
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

    @JsonIgnore
    @ManyToOne
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answerList;

    //copy constructor
    public Question(Question copy, Quiz q){
        this.id = copy.getId();
        this.questionString = copy.getQuestionString();
        this.randomOrder = copy.isRandomOrder();
        this.quiz = q;

        List<Answer> list = new ArrayList<Answer>();
        for(Answer a: copy.getAnswerList()){
            list.add(new Answer(a, this));
        }
        this.answerList = list;
    }
}
