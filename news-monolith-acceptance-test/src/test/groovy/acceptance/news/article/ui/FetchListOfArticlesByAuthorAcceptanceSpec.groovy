package acceptance.news.article.ui

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import pl.pja.s13868.news.mono.article.domain.ArticleFacade
import pl.pja.s13868.news.mono.article.domain.ArticleJavaConfig
import pl.pja.s13868.news.mono.article.domain.dto.CreateDraftArticleDto
import pl.pja.s13868.news.mono.article.domain.dto.RegisterAuthorDto
import spock.lang.Specification
import spock.lang.Stepwise

@DirtiesContext
@SpringBootTest(classes = [ArticleJavaConfig], webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Stepwise
class FetchListOfArticlesByAuthorAcceptanceSpec extends Specification {

    final AUTHOR_USER_ID = 1L
    final OTHER_USER_ID = 2L

    @Autowired
    ArticleFacade facade

    def "should list articles of given author"() {
        given: "author and one other user present"
        def registerAuthorDto = new RegisterAuthorDto(AUTHOR_USER_ID, "Krzysztof Dzido", "Java dev", "krzysztof.dzido@gmail.com", null, null, null)
        def registerOtherDto = new RegisterAuthorDto(OTHER_USER_ID, "Other Author", "...", "other.author@gmail.com", null, null, null)
        facade.registerAuthor(registerAuthorDto)
        facade.registerAuthor(registerOtherDto)

        and: "two articles of given author"
        def firstArticleOfAuthorDto = new CreateDraftArticleDto(
                AUTHOR_USER_ID,
                'text/html',
                "Draft Article",
                "Description...",
                "<html><body><h1>title2</h1>Content1...<body></html>")
        def secondArticleOfAuthorDto = new CreateDraftArticleDto(
                AUTHOR_USER_ID,
                'text/html',
                "Public Article",
                "Description...",
                "<html><body><h1>title2</h1>Content2...<body></html>")
        def idOfFirstArticle = facade.createDraftArticle(firstArticleOfAuthorDto)
        def idOfSecondArticle = facade.createDraftArticle(secondArticleOfAuthorDto)

        and: "one article of other user"
        def articleOfOtherUserDto = new CreateDraftArticleDto(
                OTHER_USER_ID,
                'text/html',
                "Other Article",
                "Other...",
                "<html><body><h1>title3</h1>Content3...<body></html>")
        def idOfOtherArticle = facade.createDraftArticle(articleOfOtherUserDto)

        when: "list articles of author"
        def articlesOfAuthor = facade.fetchListOfArticlesByAuthor(AUTHOR_USER_ID)
        then: "articles of author are returned"
        articlesOfAuthor.size() == 2
        articlesOfAuthor.any() { it.getId() == idOfFirstArticle}
        articlesOfAuthor.any() { it.getId() == idOfSecondArticle}

        when: "list articles of other user"
        def articlesOfOtherUser = facade.fetchListOfArticlesByAuthor(OTHER_USER_ID)
        then: "articles of author are returned"
        articlesOfOtherUser.size() == 1
        articlesOfOtherUser.any() { it.getId() == idOfOtherArticle}
    }


}
