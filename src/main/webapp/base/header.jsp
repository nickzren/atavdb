<header>    
    <nav class="navbar navbar-expand-lg">
        <a class="navbar-brand brand-link" href="index.jsp">
            <img src="img/cumc-logo.png" class="d-inline-block align-top brand-img" alt="">
            ATAVDB
        </a>

        <div class="collapse navbar-collapse justify-content-end">
            <div class="navbar-nav">
                <a class="nav-item nav-link home-link" href="home">Home</a>
                <a class="nav-item nav-link home-link" href="about">About</a>
                <a class="nav-item nav-link home-link" href="terms">Terms</a>
                <a class="nav-item nav-link home-link" href="contact">Contact</a>
                <c:choose>
                    <c:when test="${not empty username}" >
                        <a class="nav-item nav-link home-link" href="SignOut"><i class="fas fa-sign-out-alt"></i>&nbsp;Sign Out</a>
                    </c:when>
                    <c:otherwise>
                        <a class="nav-item nav-link home-link" href="signin"><i class="fas fa-sign-in-alt"></i>&nbsp;Sign In</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>
</header>