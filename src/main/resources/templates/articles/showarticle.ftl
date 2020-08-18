<#import "../common/standardPage.ftl" as p>


    <#if article??>
<@p.page title="${article.title}">
    <div class="col-lg-8">
    <h1 class="mt-4">${article.title}</h1>
    <p class="lead">
        by
        <#if article.author??>
            <a href="#">${article.author.username}</a>
        <#else>
            Anonymous
        </#if>
    </p>

    <hr>
        <p>${article.createdDate?string('dd.MM.yyyy HH:mm:ss')}</p>
          <hr>
          ${article.body}
          <hr>
        </div>
</@p.page>
    </#if>