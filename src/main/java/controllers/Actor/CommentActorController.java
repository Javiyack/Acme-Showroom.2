
package controllers.Actor;

import controllers.AbstractController;
import domain.Comment;
import domain.Commentable;
import domain.Item;
import domain.Showroom;
import forms.CommentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.CommentService;
import services.ItemService;
import services.ShowroomService;
import utilities.BasicosAleatorios;
import utilities.Tools;

import java.util.Collection;

@Controller
@RequestMapping("/comment/actor")
public class CommentActorController extends AbstractController {

    // Supporting services -----------------------------------------------------

    @Autowired
    private CommentService commentService;
    @Autowired
    private ShowroomService showroomService;
    @Autowired
    private ItemService itemService;

    // Constructors -----------------------------------------------------------

    public CommentActorController() {
        super();
    }


    // List ------------------------------------------------------------------
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(Integer objectId, final Integer pageSize, String path) {
        ModelAndView result;
        final Collection <Comment> comments;
        if (objectId != null) {
            comments = this.commentService.findByCommentedObjectId(objectId);
            Commentable commentedObject = this.commentService.findCommentedObjectByCommentedObjectId(objectId);
            result = new ModelAndView("comment/list");
            result.addObject("path", path);
            result.addObject("commented", commentedObject.getObjectName());
        } else {
            comments = this.commentService.findAll();
            result = new ModelAndView("comment/list");
        }
        result.addObject("comments", comments);
        result.addObject("requestUri", "comment/actor/list.do");
        result.addObject("pageSize", (pageSize != null) ? pageSize : 5);
        return result;
    }


    // Display user -----------------------------------------------------------
    @RequestMapping(value = "/display", method = RequestMethod.GET)
    public ModelAndView display(@RequestParam final int commentId, String path) {
        ModelAndView result;

        try {
            final Comment comment = this.commentService.findOne(commentId);
            Assert.notNull(comment, "msg.not.found.resource");
            final CommentForm commentForm = new CommentForm(comment);
            commentForm.setPath(path);
            result = new ModelAndView("comment/display");
            Commentable target = commentService.findCommentedObjectByCommentedObjectId(comment.getCommentedObjectId());
            commentForm.setPath(path);
            commentForm.setTargetName(target.getObjectName());
            result.addObject("commentForm", commentForm);
            result.addObject("display", true);

        } catch (Throwable oops) {
            if (oops.getMessage().startsWith("msg.")) {
                return createMessageModelAndView(oops.getLocalizedMessage(), "/");
            } else {
                return this.createMessageModelAndView("panic.message.text", "/");
            }
        }
        return result;
    }

    // Create ---------------------------------------------------------------

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam final int objectId, String path) {
        ModelAndView result;
        final Comment comment = commentService.create();
        Commentable target = commentService.findCommentedObjectByCommentedObjectId(objectId);
        comment.setCommentedObjectId(objectId);
        result = this.createEditModelAndView(comment);
        final CommentForm commentForm = new CommentForm(comment);
        commentForm.setPath(path);
        commentForm.setTargetName(target.getObjectName());
        result.addObject("commentForm", commentForm);
        return result;
    }

    // Edit  -----------------------------------------------------------
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam final int commentId, String path) {
        ModelAndView result;

        try {
            final Comment comment = this.commentService.findOne(commentId);
            Assert.notNull(comment, "msg.not.found.resource");
            result = this.createEditModelAndView(comment);
            Commentable target = commentService.findCommentedObjectByCommentedObjectId(comment.getCommentedObjectId());
            final CommentForm commentForm = new CommentForm(comment);
            commentForm.setPath(path);
            commentForm.setTargetName(target.getObjectName());
            result.addObject("commentForm", commentForm);
            result.addObject("display", false);

        } catch (Throwable oops) {
            if (oops.getMessage().startsWith("msg.")) {
                return createMessageModelAndView(oops.getLocalizedMessage(), "/");
            } else {
                return this.createMessageModelAndView("panic.message.text", "/");
            }
        }
        return result;
    }


    // Save mediante Post ---------------------------------------------------

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
    public ModelAndView save(CommentForm commentForm, final BindingResult binding) {
        ModelAndView result;
        Comment comment = commentService.reconstruct(commentForm, binding);
        if (binding.hasErrors()) {
            result = this.createEditModelAndView(comment);
            result.addObject("commentForm", commentForm);
        } else
            try {
                comment = this.commentService.save(comment);
                result = new ModelAndView("comment/display");
                result.addObject("commentForm", commentForm);
                result.addObject("display", true);
                result.addObject("info", "msg.commit.ok");
            } catch (final Throwable oops) {
                if (oops.getMessage().startsWith("msg.")) {
                    return createMessageModelAndView(oops.getLocalizedMessage(), "/");
                } else {
                    result = this.createEditModelAndView(comment, "msg.commit.error");
                }
            }
        return result;
    }

    // Create a number of Random Items por showroom ---------------------------------------------------------------

    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    public ModelAndView createRandom(Integer number) {
        ModelAndView result = new ModelAndView("redirect:/");
        Collection <Showroom> showrooms = showroomService.findAll();
        for (Showroom showroom : showrooms) {
            number = BasicosAleatorios.getNumeroAleatorio(3);

            for (int i = 0; i < number; i++) {
                Comment comment = generateComment(showroom.getId());
                commentService.save(comment);
            }
            Collection <Item> items = itemService.findByShowroom(showroom);
            for (Item item : items) {
                number = BasicosAleatorios.getNumeroAleatorio(2);
                for (int i = 0; i < number; i++) {
                    Comment comment = generateComment(item.getId());
                    commentService.save(comment);
                }
            }
        }
        return result;
    }

    private Comment generateComment(int objectId) {
        Comment comment = commentService.create();
        comment.setTitle(Tools.generateBussinesName());
        comment.setText(Tools.generateDescription());
        comment.setRating((BasicosAleatorios.getNumeroAleatorio(4)));
        comment.setCommentedObjectId(objectId);
        for (int n = 0; n < BasicosAleatorios.getNumeroAleatorio(5); n++) {
            comment.getPictures().add(Tools.getUrlAleatoria());
        }
        return comment;
    }


    // Auxiliary methods -----------------------------------------------------
    protected ModelAndView createEditModelAndView(final Comment model) {
        final ModelAndView result;
        result = this.createEditModelAndView(model, null);
        return result;
    }

    protected ModelAndView createEditModelAndView(final Comment model, final String message) {
        final ModelAndView result;
        final CommentForm commentForm = new CommentForm(model);
        result = new ModelAndView("comment/edit");
        result.addObject("comment", model);
        result.addObject("commentForm", commentForm);
        result.addObject("requestUri", "comment/actor/create.do");
        result.addObject("edition", true);
        result.addObject("creation", model.getId() == 0);
        result.addObject("message", message);

        return result;

    }

}
