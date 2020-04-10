<%@page import="model.CalledVariant"%>

<%
    CalledVariant variant = (CalledVariant) request.getAttribute("variant");
%>

<div class="row">    
    <div class="col-md-10">
        <h4>
            <mark>Query: <%=request.getAttribute("query")%></mark>
                <%if (variant != null) {%>
            <span class="label label-default" data-toggle="tooltip" 
                  title="">
                Sample Count: <%=variant.getNS()%>
            </span>
            &nbsp;
            <span class="label label-default" data-toggle="tooltip" 
                  title="Allele Frequency of variant">
                AF: <%=variant.getAF()%>
            </span>
            <%}%>
        </h4>
    </div>
</div>

<%
    if (variant == null) {
%>

<br/>

<div class="alert alert-warning" style="width:24%">
    No results found from search query.
</div>

<%
} 
%>
