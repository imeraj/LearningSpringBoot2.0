package com.meraj.microservices.images.controller;

import com.meraj.microservices.images.model.Comment;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@Controller
@EnableBinding(Source.class)
public class CommentController {
    private FluxSink<Message<Comment>> commentSink;
    private Flux<Message<Comment>> flux;

    public CommentController() {
        this.flux = Flux.<Message<Comment>>create(
            emitter -> this.commentSink = emitter,
            FluxSink.OverflowStrategy.IGNORE)
            .publish()
            .autoConnect();
    }

    @PostMapping("/comments")
    public Mono<String> addComment(Mono<Comment> newComment) {
        if (commentSink != null) {
            return newComment
                    .map(comment -> {
                        commentSink.next(
                                MessageBuilder
                                .withPayload(comment)
                                .setHeader(MessageHeaders.CONTENT_TYPE,
                                        MediaType.APPLICATION_JSON_VALUE)
                                .build());
                        return comment;
                    })
                    .flatMap(comment -> Mono.just("redirect:/"));
        } else {
            return Mono.just("redirect:/");
        }
    }

    @StreamEmitter
    @Output(Source.OUTPUT)
    public void emit(FluxSender output) {
        output.send(this.flux);
    }
}
