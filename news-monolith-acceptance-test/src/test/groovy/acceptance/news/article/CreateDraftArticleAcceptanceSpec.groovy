package acceptance.news.article

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.pja.s13868.news.mono.article.domain.ArticleFacade
import pl.pja.s13868.news.mono.article.domain.ArticleJavaConfig
import pl.pja.s13868.news.mono.article.domain.dto.ArticleDto
import pl.pja.s13868.news.mono.article.domain.dto.CreateDraftArticleDto
import spock.lang.Specification
import spock.lang.Stepwise


// TODO this is component test i guess, the domain component strictly speaking??
@SpringBootTest(classes = [ArticleJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class CreateDraftArticleAcceptanceSpec extends Specification {

    final AUTHOR_ID = 1L

    final DATE_CREATE = null
    final DATE_PUBLISH = null

    @Autowired
    ArticleFacade facade

    def "should create a draft article successfully"() {
        given:
        def draft = new CreateDraftArticleDto(
                AUTHOR_ID,
                'text/html',
                "Title",
                "Description...",
                "<html><body><h1>title</h1>Content...<body></html>")

        when: "we create a draft article"
        def idOfDraft = facade.createDraftArticle(draft)

        then: "system has this article"
        def article = facade.getArticle(idOfDraft).get()
        article.id == idOfDraft
        article.status == ArticleDto.Status.DRAFT
        article.authorId == AUTHOR_ID
        article.mimeType == draft.mimeType
        article.title == draft.title
        article.description == draft.description
        and:
        article.creationDate == null
        article.publicationDate == null
    }

    // TODO negative test cases

}
