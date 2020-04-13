<%@page import="model.CalledVariant"%>

<%
    CalledVariant variant = (CalledVariant) request.getAttribute("variant");
    String query = (String) request.getAttribute("query");
%>

<div class="row">    
    <div class="col-md-12">
        <%
            if (query != null) {
        %>
        <mark>Query: <%=query%></mark>
        <br><br>
        <%
            if (variant == null) {
        %>
        <div class="alert alert-warning" style="width:50%">
            No results found from search query.
        </div>
        <%
        } else {
        %>
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr class="text-center">
                        <th class="align-middle" scope="col">Variant ID</th>
                        <th class="align-middle" scope="col">Effect</th>
                        <th class="align-middle" scope="col">Gene</th>
                        <th class="align-middle" scope="col">AC</th>
                        <th class="align-middle" scope="col">AN</th>
                        <th class="align-middle" scope="col">AF</th>
                        <th class="align-middle" scope="col">NS</th>
                        <th class="align-middle" scope="col">Number of homozygotes</th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="text-center">
                        <td class="align-middle"><%=variant.getVariantIdStr()%></td>
                        <td class="align-middle"><%=variant.getEffect()%></td>
                        <td class="align-middle"><%=variant.getGeneName()%></td>
                        <td class="align-middle"><%=variant.getAC()%></td>
                        <td class="align-middle"><%=variant.getAN()%></td>
                        <td class="align-middle"><%=variant.getAF()%></td>
                        <td class="align-middle"><%=variant.getNS()%></td>
                        <td class="align-middle"><%=variant.getNH()%></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <%
                }
            }
        %>
    </div>
</div>
