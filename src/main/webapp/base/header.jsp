<header>    
    <nav class="navbar navbar-expand-lg">
        <a class="navbar-brand brand-link" href="./">
            <img src="<c:url value="/img/cumc-logo.png" />" width="43" height="43" class="d-inline-block align-top" alt="" loading="lazy">
            <div class="navbar-brand">ATAVDB</div>
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <i class="fas fa-bars"></i>
        </button>

        <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
            <div class="navbar-nav">
                <a class="nav-item nav-link home-link" href="<c:url value="/" />">Home</a>
                <a class="nav-item nav-link home-link" href="<c:url value="/about" />">About</a>
                <a class="nav-item nav-link home-link" href="<c:url value="/terms" />">Terms</a>
                <a class="nav-item nav-link home-link" href="<c:url value="/contact" />">Contact</a>
                <c:choose>
                    <c:when test="${not empty username}" >
                        <a class="nav-item nav-link home-link" href="<c:url value="/signout" />"><i class="fas fa-sign-out-alt"></i>&nbsp;Sign Out</a>
                    </c:when>
                    <c:otherwise>
                        <a class="nav-item nav-link home-link" href="<c:url value="/signin" />"><i class="fas fa-sign-in-alt"></i>&nbsp;Sign In</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>                         
</header>