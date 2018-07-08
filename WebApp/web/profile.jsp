<!DOCTYPE html>
<%@ taglib uri="META-INF/tagLib.tld" prefix="tagLib" %>
<tagLib:ControlAccess />
<html>
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Stock</title>
  <!-- Tell the browser to be responsive to screen width -->
  <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
  <!-- Bootstrap 3.3.6 -->
  <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
  <!-- Ionicons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
  <!-- Theme style -->
  <link rel="stylesheet" href="dist/css/AdminLTE.min.css">
  <!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
  <link rel="stylesheet" href="dist/css/skins/_all-skins.min.css">

</head>
<body class="hold-transition skin-blue layout-top-nav">
<!-- Site wrapper -->
<header class="main-header">
  <nav class="navbar navbar-static-top">
    <div class="container-fluid">
    <div class="navbar-header">
      <b class="navbar-brand">Stock</b>
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
        <i class="fa fa-bars"></i>
      </button>
    </div>
    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="navbar-collapse">
      <ul class="nav navbar-nav navbar-right">
        <li><a href="Logout">Logout</a></li>
      </ul>
    </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
  </nav>
</header>
<div class="text-center">
    <h1>Welcome <tagLib:NameSurname /></h1>
</div>
<section style="margin-bottom: 40px;">
    <div class="box-body">
      <tagLib:TitleTable />
    </div>
    <div class="text-center">
        <h4>Add title</h4>
        <center>
        <form action="AddTitle" method="post">
            <div class="input-group input-group-sm" style="width: 150px;">
                <select name="title" class="form-control">
                    <tagLib:TitleMissingList />
                </select>
                    <span class="input-group-btn">
                      <input type="submit" value="Add" class="btn btn-info btn-flat" style="background-color: #408EBA; border-color: #408EBA;">
                    </span>
              </div>
        </form>
        </center>
    </div>
</section>
<footer style="background-color: white; position: fixed; bottom: 0px; width: 100%;">
    <div style="padding-left: 10px;">
        <h4>Giacomo Chiarot 854893</h4>
        <p>
            Web Application Stock.
            Documentation <a href="documentation.html">here.</a>
        </p>
    </div>
</footer>
<!-- jQuery 2.2.3 -->
<script src="plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="bootstrap/js/bootstrap.min.js"></script>
<!-- SlimScroll -->
<script src="plugins/slimScroll/jquery.slimscroll.min.js"></script>
<!-- FastClick -->
<script src="plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/app.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>
</body>
</html>
