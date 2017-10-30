package acceptance.news.article

import org.joda.time.DateTimeUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.article.domain.ArticleFacade
import pl.pja.s13868.news.mono.article.domain.ArticleJavaConfig
import pl.pja.s13868.news.mono.article.domain.dto.ArticleDto
import pl.pja.s13868.news.mono.article.domain.dto.CreateDraftArticleDto
import pl.pja.s13868.news.mono.article.domain.dto.RegisterAuthorDto
import spock.lang.Specification
import spock.lang.Stepwise

@DirtiesContext
@SpringBootTest(classes = [ArticleJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class CreateArticleAcceptanceSpec extends Specification {

    final USER_ID = 1L
    final DATE_CREATE = new Date().getTime()

    @Autowired
    ArticleFacade facade

    void setup() {
        DateTimeUtils.setCurrentMillisFixed(DATE_CREATE)
    }

    void cleanup() {
        DateTimeUtils.setCurrentMillisSystem()
    }

    def "should create a draft articles"() {
        given: "author registered"
        def registerAuthorDto = new RegisterAuthorDto(USER_ID, "Krzysztof Dzido", "Java dev", "krzysztof.dzido@gmail.com", null, null, null)
        facade.registerAuthor(registerAuthorDto)

        when: "we create a draft article"
        def createArticleDto = new CreateDraftArticleDto(
                USER_ID,        // TODO should be retrieved inside of facade?
                'text/html',
                "Title",
                "Description...",
                "<html><body><h1>title</h1>Content...<body></html>")
        def idOfDraft = facade.createDraftArticle(createArticleDto)

        then: "system has this article"
        def article = facade.getDraftArticle(idOfDraft).get()
        article.id == idOfDraft
        article.version == 0L
        article.status == ArticleDto.Status.DRAFT
        article.authorId == USER_ID
        article.mimeType == createArticleDto.mimeType
        article.title == createArticleDto.title
        article.description == createArticleDto.description
        and:
        article.publicationStartDate == null
        article.publicationEndDate == null
        and:
        article.creationDate.getTime() == DATE_CREATE
        and: "article is not published"
        facade.fetchListOfPublishedArticles().any {it.id == idOfDraft} == false
    }

    // TODO multiple articles by multiple authors

    // TODO negative test cases



}
