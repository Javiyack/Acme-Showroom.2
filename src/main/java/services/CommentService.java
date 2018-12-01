package services;

import domain.Actor;
import domain.Comment;
import domain.Commentable;
import forms.CommentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.CommentRepository;

import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class CommentService {

    //Repositories
    @Autowired
    private CommentRepository commentRepository;

    //Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private Validator validator;

    //Constructor
    public CommentService() {
        super();
    }

    // Simple CRUD methods ----------------------------------------------------

    //Create
    public Comment create() {
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        final Comment result = new Comment();
        result.setMoment(new Date());
        result.setRating(0);
        result.setActor(actor);
        return result;
    }

    public Comment save(Comment comment) {
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(comment.getCommentedObjectId()!=null && comment.getCommentedObjectId()!=0, "msg.save.first");
        comment.setMoment(new Date(System.currentTimeMillis()-200));
        comment = commentRepository.save(comment);
        this.flush();
        return comment;
    }

    public Comment findOne(int followId) {
        return commentRepository.findOne(followId);
    }


    public Collection <Comment> findByCommentedObjectId(Integer objectId) {
        return commentRepository.findByCommentedObjectId(objectId);    }
    public Commentable findCommentedObjectByCommentedObjectId(Integer objectId) {
        return commentRepository.findCommentedObjectByCommentedObjectId(objectId);    }

    public Comment reconstruct(Comment comment, BindingResult binding ) {
        Actor actor = actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        comment.setActor(actor);
        this.validator.validate(comment, binding);
        return comment;
    }
    public Comment reconstruct(CommentForm commentForm, BindingResult binding ) {
        Comment comment;
        if(commentForm.getId()!=0){
            comment = commentRepository.findOne(commentForm.getId());
        }else{
            Actor actor = actorService.findByPrincipal();
            Assert.notNull(actor, "msg.not.logged.block");
            comment = this.create();
            comment.setActor(actor);
            comment.setRating(commentForm.getRating());
            comment.setMoment(new Date());
            comment.setTitle(commentForm.getTitle());
            comment.setText(commentForm.getText());
            comment.setPictures(commentForm.getPictures());
            comment.setCommentedObjectId(commentForm.getCommentedObjectId());
            commentForm.setActor(actor);
            this.validator.validate(comment, binding);
        }
        return comment;
    }

    public Collection<Comment> findAll() {
        return this.commentRepository.findAll();
    }

    public void flush() {
        commentRepository.flush();
    }
}
