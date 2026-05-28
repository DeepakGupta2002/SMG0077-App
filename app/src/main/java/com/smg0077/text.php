<?php
/* Database config */
$servername = "localhost";
$username = "u888916560_matka";
$password = "Matka@1432";
$db = "u888916560_matka";

date_default_timezone_set("Asia/Calcutta");

$conn = mysqli_connect($servername, $username, $password, $db);


$cur_date = date("Y-m-d");
$cur_date_1 = date("d-m-Y");
$day = date("l");
// $cur_date = "2022-12-09";

$sql = "SELECT tb_weekday_games.*,tb_games.* FROM `tb_weekday_games` LEFT JOIN `tb_games` On tb_weekday_games.game_id = tb_games.game_id   WHERE tb_games.status = '1'AND tb_weekday_games.name='" . $day . "'";


// $sql = "SELECT tb_game_result_history.*,tb_games.* FROM `tb_game_result_history` LEFT JOIN `tb_games` On tb_game_result_history.game_id = tb_games.game_id LEFT JOIN promotions WHERE tb_games.status = '1' AND tb_game_result_history.result_date = '" . $cur_date . "' ";

//$sql="SELECT tb_game_result_history.*,tb_bid_history.*,tb_weekday_games.* FROM `tb_bid_history` LEFT JOIN `tb_game_result_history` On tb_game_result_history.game_id = tb_bid_history.game_id LEFT JOIN `tb_weekday_games` On tb_weekday_games.game_id = tb_bid_history.game_id WHERE tb_bid_history.status = '1' AND tb_bid_history.bid_date = '" . $cur_date . "' AND tb_weekday_games.name='" . $day . "'";

//$sql = "SELECT tb_game_result_history.*,tb_bid_history.*,tb_weekday_games.* FROM `tb_bid_history` LEFT JOIN `tb_game_result_history` On tb_game_result_history.game_id = tb_bid_history.game_id LEFT JOIN `tb_weekday_games` On tb_weekday_games.game_id = tb_bid_history.game_id WHERE tb_bid_history.status = '1' AND tb_bid_history.bid_date = '" . $cur_date . "' AND tb_game_result_history.result_date='" . $cur_date . "' AND tb_weekday_games.name='" . $day . "'";
//(SELECT tb_bid_history.*,tb_weekday_games.* FROM `tb_bid_history` LEFT JOIN `tb_weekday_games` On tb_weekday_games.game_id = tb_bid_history.game_id  WHERE tb_bid_history.status = '1' AND tb_bid_history.bid_date = '2022-12-13' AND tb_weekday_games.name='Tuesday');
//$sql = "SELECT tb_game_result_history.*,tb_bid_history.*,tb_weekday_games.* FROM `tb_bid_history` LEFT JOIN `tb_game_result_history` On tb_game_result_history.game_id = tb_bid_history.game_id LEFT JOIN `tb_weekday_games` On tb_weekday_games.game_id = tb_bid_history.game_id WHERE tb_bid_history.status = '1' AND tb_bid_history.bid_date = '" . $cur_date . "'  AND tb_weekday_games.name='" . $day . "'";

//$sql= "SELECT tb_game_result_history.*,tb_bid_history.*,tb_games.* FROM `tb_game_result_history` LEFT JOIN `tb_bid_history` On tb_game_result_history.game_id = tb_bid_history.game_id LEFT JOIN `tb_games` On tb_games.game_id = tb_bid_history.game_id WHERE tb_bid_history.status = '1' AND tb_bid_history.bid_date = '" . $cur_date . "' AND tb_game_result_history.result_date='".$cur_date."'";


//$sql="SELECT tb_bid_history.*,tb_weekday_games.* FROM `tb_bid_history` LEFT JOIN `tb_weekday_games` On tb_weekday_games.game_id = tb_bid_history.game_id WHERE tb_bid_history.status = '1' AND tb_bid_history.bid_date = '" . $cur_date . "'  AND tb_weekday_games.name='" . $day . "'";
// echo "<pre>";
// print_r($sql);
// die();

$sff = mysqli_query($conn, $sql);
// echo "<pre>";
// print_r($sff);
// die();
?>

<?php
$sqlwt = "SELECT * FROM tb_contact_settings";
$result = $conn->query($sqlwt);
if ($result->num_rows > 0) {
  $getdatas =  $result->fetch_assoc();
  $wtnumber = $getdatas['whatsapp_no'];
  $telolink = $getdatas['mobile_2'];
}else{
  $wtnumber = '';
  $telolink = '';
}

?>

<html lang="en">

<head>
      <meta http-equiv="content-type" content="text/html;charset=UTF-8">
      <meta charset="UTF-8">
      <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1">

  <!--<title>matka King Matka</title>-->
  <title>matka King Game | Play Online Game</title>
  <meta name="description" content="matka King Game is a very basic online game where you are required to guess numbers that range between 0 to 9.  Anyone can become a master of the matka King.">
  <!--
  <meta name="keywords" content="" />
        <meta name="description" content="" />  -->
  <meta name="keywords" content="matka King Game,matka King, matka King result,Kalyan bazar,Reddy Anna result, matka King, matka King download" />
   <!--  <link rel="canonical" href="http://sincar.eu/Kalyanstar/Kalyan-star-online-landingpage-html/" /> -->
    <meta charset="UTF-8">
    <meta property="og:title" content="Play matka King Game">
    <meta property="og:type" content="matka King Game ">
  <!--  <meta property="og:url" content="http://sincar.eu/Kalyanstar/Kalyan-star-online-landingpage-html/"> -->
    <meta property="og:image" content="<?php echo base_url(); ?>assets/img/logo-2.png">
    <meta property="og:site_name" content="matka King">
    <meta property="og:description" content="matka King is a very basic online game where you are required to guess numbers that range between 0 to 9.  Anyone can become a master of the matka King." />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="shortcut icon" href="<?php echo base_url(); ?>assets/img/fv.png" type="image/x-icon">
    <link rel="icon" href="<?php echo base_url(); ?>assets/img/fv.png" type="image/x-icon">
    <!-- CSS only -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.1.0/css/all.css" rel="stylesheet">
     <link type="text/css" rel="stylesheet" href="<?php echo base_url(); ?>assets/css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="<?php echo base_url(); ?>assets/css/animate.css">
    <link type="text/css" rel="stylesheet" href="<?php echo base_url(); ?>assets/css/newfont.css">
    <link type="text/css" rel="stylesheet" href="<?php echo base_url(); ?>assets/css/owl.carousel.css">
    <link type="text/css" rel="stylesheet" href="<?php echo base_url(); ?>assets/css/fontawesome-all.css">
    <link type="text/css" rel="stylesheet" href="<?php echo base_url(); ?>assets/css/swiper.css">
    <link type="text/css" rel="stylesheet" href="<?php echo base_url(); ?>assets/css/jquery.bxslider.css">
    <link type="text/css" rel="stylesheet" href="<?php echo base_url(); ?>assets/css/jquery.mCustomScrollbar.min.css">
    <link type="text/css" rel="stylesheet" href="<?php echo base_url(); ?>assets/css/odometer-theme-default.css">
    <link type="text/css" rel="stylesheet" href="<?php echo base_url(); ?>assets/css/flaticon.css">
    <link type="text/css" rel="stylesheet" href="<?php echo base_url(); ?>assets/css/style.css">

<!-- Google Tag Manager -->
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-5JP4HQ4');</script>
<!-- End Google Tag Manager -->

<!--Real front-->
    <!--<script type="application/ld+json">
{
  "@context": "https://schema.org/",
  "@type": "WebSite",
  "name": "matka King",
  "url": "https://Kalyanstaronline/App/",
  "potentialAction": {
    "@type": "SearchAction",
    "target": "{search_term_string}",
    "query-input": "required name=search_term_string"
  }
}
</script>
<script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "Organization",
  "name": "matka King",
  "url": "https://Kalyanstaronline/App/",
  "logo": "https://Kalyanstaronline/App/asset/assets/img/logo-2.png",
  "sameAs": [
    "https://www.facebook.com/groups/2364703423626623/?ref=share",
    "https://www.youtube.com/channel/UC9UqQLvobvt3_ZiYmu2FYAA/featured"
  ]
}
</script>
<script type="application/ld+json">
{
  "@context": "https://schema.org",
  "@type": "FAQPage",
  "mainEntity": [{
    "@type": "Question",
    "name": "What is matka King?",
    "acceptedAnswer": {
      "@type": "Answer",
      "text": "matka King Game is a game of perfect guess and a game of applying your Luck. People play the matka King game with utmost passion and an oversized scale. This game is quite fashionable as it is a game of mind it will hold you until the end."
    }
  },{
    "@type": "Question",
    "name": "What is the History of matka King?",
    "acceptedAnswer": {
      "@type": "Answer",
      "text": "For the first time, the game was played around 1961 in the Worli region of India. During the 1980s And 1990s this game become so popular. This is considered as one of the most exciting and very loving games in which you can win cash prize just by playing the game. So this makes the matka King game growing so fast in India."
    }
  },{
    "@type": "Question",
    "name": "Why matka King is popular in India?",
    "acceptedAnswer": {
      "@type": "Answer",
      "text": "In India, matka King game is that enables you to win an outsized chunk of cash. In this game, you can win money through speculations. Yes, this game has now become popular as one of the most lotteries winning game in the country."
    }
  },{
    "@type": "Question",
    "name": "What are the types of matka King?",
    "acceptedAnswer": {
      "@type": "Answer",
      "text": "Here are a few well known or most popular matka King games;
Matka: A game in which a Matka is used to draw numbers, the word Matka is a Hindi word which English meaning is earthen pot. As to play this game we use this so we called it a Matka game.
Single: This kind of matka King game is based on number Guessing. Yes, you have to choose a number in between 0 to 9 and betting over that number.
Jodi/Pair: This is also a number guessing game like Single. But in this case, you have to bet over any pair of digits from 00 to 99.
Patti/Panna: There is another variant in the list called Patti and Panna. This is the result of some three digits which come as a result of betting and thus the smartest thing is that every three-digit number is Patti/Panna but you only use a limited three-digit number together. And this increases the range of the game.
Open Result / Close Result: And it is full of enthusiasm. You may not know but you find two parts in the results of this matka King game."
    }
  },{
    "@type": "Question",
    "name": "How can I play satta online?",
    "acceptedAnswer": {
      "@type": "Answer",
      "text": "Thanks to digital transformation, playing matka King has become easier than ever. With our matka King app, we bring you an entertaining and engaging world of matka King guessing games. With a simple matka King download, you can enjoy the game anywhere anytime, and become the Reddy Anna King. Moreover, downloading this app will not cost you anything."
    }
  },{
    "@type": "Question",
    "name": "How can I bet Matka?",
    "acceptedAnswer": {
      "@type": "Answer",
      "text": "The Matka guessing game is based on a random selection of numbers and placing your bets. Players have to choose a number between 0 to 9 and lastly, the chit is drawn digitally to reveal the winners. In the Starr Kalyan app, all the games are mentioned in the dashboard, so you can directly choose the game and start Matka guessing without hassle. You will be shown the Reddy Anna results after the game time is up."
    }
  },{
    "@type": "Question",
    "name": "How is Satta king number calculated?",
    "acceptedAnswer": {
      "@type": "Answer",
      "text": "Winning in Reddy Anna is all about how well you are at guessing the numbers. There will be two draws and in the first draw, you have to pick three numbers from 0 to 9. The final number that comes out is the addition of all the three numbers that you have chosen. For instance, if you have selected 2, 5, and 6 and the final number would be 13. And, if you want to use only one digit of the final number then it would be 4 and your first draw will look something like this- 2, 5, and 6 x 3.
Coming to the second pick of number, is drawn the same way as the first day. You can choose any random number for example 2, 4, and 8. Adding them together will you a sum of 13. Out of the final number, you will use the last digit, hence your final pick will look be 2, 4, and, 8 x3.
In the end, your card will appear like= (2, 5, and 6 x 3) X (2, 4, and, 8 x3)"
    }
  },{
    "@type": "Question",
    "name": "How many types of Matka gambling games are there?",
    "acceptedAnswer": {
      "@type": "Answer",
      "text": "There are two types of Matka gambling games which include Reddy Anna and Worli Matka. While the Reddy Anna runs throughout the week, Worli Matka runs only 5 days a week (Monday to Friday)."
    }
  },{
    "@type": "Question",
    "name": "Who is the real Matka King?",
    "acceptedAnswer": {
      "@type": "Answer",
      "text": "Ratan Khatri is known as the Matka King and he controlled the nationwide illegal gambling networks between 1960 to 1990. He had many international connections and hundreds of punters and dealers spawned across the globe."
    }
  }]
}
</script>-->


    <style>
        .navbar-nav .nav-link.active {color:#865f23;}
        .table td {font-weight: bold;}
        .dia-footer-social i {font-size: 22px;}
        .dia-footer-shape3 {z-index: 1;}
        .newbg {background: rgb(254,201,50);
background: radial-gradient(circle, rgba(254,201,50,1) 0%, rgba(233,159,29,1) 52%, rgba(211,117,7,1) 100%);}


        @media screen and (max-width: 600px) {
            .ei-faq-section .fq-shape-style1 { display:none;}
            .ei-faq-section .fq-shape-style2 { display:none;}
            .ei-faq-section .fq-shape-style3 { display:none;}
            .ei-faq-section .fq-shape-style4 { display:none;}
            .ei-faq-section .fq-shape-style5 { display:none;}
            .ei-faq-section .fq-shape-style6 { display:none;}


}
    </style>
   </head>
<!--<body data-spy="scroll" data-target="#navbarCodeply" data-offset="70" style="overflow: visible;">-->

<body class="dia-home" data-spy="scroll" data-target=".dia-main-navigation" data-offset="80">



    <!-- preloader - start -->
    <div class="up">
        <a href="#" id="scrollup" class="dia-scrollup text-center"><i class="fas fa-angle-up"></i></a>
        <a tooltip="Whatsapp" href="https://wa.me/<?=$wtnumber?>?text=Hello" style="display: inline;
    background-color: #00ff0a;bottom:140px;" class="dia-scrollup text-center" target="_blank">
            <svg xmlns="http://www.w3.org/2000/svg" version="1.1" xmlns:xlink="http://www.w3.org/1999/xlink"  x="0" y="0" viewBox="0 0 176 176" style="enable-background:new 0 0 512 512" xml:space="preserve" class=""><g><g data-name="Layer 2"><g data-name="09.whatsapp"><circle cx="88" cy="88" r="88" fill="#29a71a" opacity="1" data-original="#29a71a" class=""></circle><g fill="#fff"><path d="M126.8 49.2a54.57 54.57 0 0 0-87.42 63.13l-5.79 28.11a2.08 2.08 0 0 0 .33 1.63 2.11 2.11 0 0 0 2.24.87l27.55-6.53A54.56 54.56 0 0 0 126.8 49.2zm-8.59 68.56a42.74 42.74 0 0 1-49.22 8l-3.84-1.9-16.89 4 .05-.21 3.5-17-1.88-3.71a42.72 42.72 0 0 1 7.86-49.59 42.73 42.73 0 0 1 60.42 0 2.28 2.28 0 0 0 .22.22 42.72 42.72 0 0 1-.22 60.19z" fill="#ffffff" opacity="1" data-original="#ffffff" class=""></path><path d="M116.71 105.29c-2.07 3.26-5.34 7.25-9.45 8.24-7.2 1.74-18.25.06-32-12.76l-.17-.15C63 89.41 59.86 80.08 60.62 72.68c.42-4.2 3.92-8 6.87-10.48a3.93 3.93 0 0 1 6.15 1.41l4.45 10a3.91 3.91 0 0 1-.49 4l-2.25 2.92a3.87 3.87 0 0 0-.35 4.32c1.26 2.21 4.28 5.46 7.63 8.47 3.76 3.4 7.93 6.51 10.57 7.57a3.82 3.82 0 0 0 4.19-.88l2.61-2.63a4 4 0 0 1 3.9-1l10.57 3a4 4 0 0 1 2.24 5.91z" fill="#ffffff" opacity="1" data-original="#ffffff" class=""></path></g></g></g></g></svg>
        </a>
        <a class="dia-scrollup22" tooltip="Whatsapp" href="<?=$telolink?>" style="display: inline;
    background-color: #039BE5;bottom:80px;" class="dia-scrollup text-center" target="_blank">
 <svg xmlns="http://www.w3.org/2000/svg" version="1.1" xmlns:xlink="http://www.w3.org/1999/xlink" width="35" height="35" x="0" y="0" viewBox="0 0 24 24" style="enable-background:new 0 0 512 512" xml:space="preserve" class=""><g><path fill="#ffffff" d="m9.417 15.181-.397 5.584c.568 0 .814-.244 1.109-.537l2.663-2.545 5.518 4.041c1.012.564 1.725.267 1.998-.931L23.93 3.821l.001-.001c.321-1.496-.541-2.081-1.527-1.714l-21.29 8.151c-1.453.564-1.431 1.374-.247 1.741l5.443 1.693L18.953 5.78c.595-.394 1.136-.176.691.218z" opacity="1" data-original="#039be5" class=""></path></g></svg>
</a>
    </div>
    <!-- Start of header section
        ============================================= -->
        <header id="dia-header" class="dia-main-header">
            <div class="container">
                <div class="dia-main-header-content clearfix">
                    <div class="dia-logo float-left">
                    <a href="#"><img class="lazyload" src="<?php echo base_url(); ?>assets/img/logo-2.png" alt="matka King"></a>
                    </div>
                    <div class="dia-main-menu-item float-right">
                        <nav class="rounded dia-main-navigation  clearfix ul-li">
                            <ul id="main-nav" class="navbar-nav text-capitalize clearfix">
                                <li><a class="nav-link" href="#dia-banner">Home</a></li>
                                <li><a class="nav-link" href="#dia-service">About Us</a></li>
                                <li><a class="nav-link" href="#dia-about">Live Chart</a></li>
                                <li><a class="nav-link" href="#dia-portfolio">How to Play</a></li>

                      <!--<li><a class="nav-link" href="/blog">blog</a></li>-->

                            </ul>
                        </nav>
                    </div>
                </div>
                <!-- /desktop menu -->
                <div class="dia-mobile_menu relative-position">
                    <div class="dia-mobile_menu_button dia-open_mobile_menu">
                        <!--<i class=""><img src="<?php echo base_url(); ?>assets/img/bars-icon-image.png"></i>-->
                        <svg xmlns="http://www.w3.org/2000/svg" version="1.1" xmlns:xlink="http://www.w3.org/1999/xlink" width="25" height="25" x="0" y="0" viewBox="0 0 512 512" style="enable-background:new 0 0 512 512" xml:space="preserve" class="hovered-paths"><g><path d="M128 102.4c0-14.138 11.462-25.6 25.6-25.6h332.8c14.138 0 25.6 11.462 25.6 25.6S500.538 128 486.4 128H153.6c-14.138 0-25.6-11.463-25.6-25.6zm358.4 128H25.6C11.462 230.4 0 241.863 0 256c0 14.138 11.462 25.6 25.6 25.6h460.8c14.138 0 25.6-11.462 25.6-25.6 0-14.137-11.462-25.6-25.6-25.6zm0 153.6H256c-14.137 0-25.6 11.462-25.6 25.6 0 14.137 11.463 25.6 25.6 25.6h230.4c14.138 0 25.6-11.463 25.6-25.6 0-14.138-11.462-25.6-25.6-25.6z" fill="#ffffff" opacity="1" data-original="#000000" class="hovered-path"></path></g></svg>
                    </div>
                    <div class="dia-mobile_menu_wrap">
                        <div class="mobile_menu_overlay dia-open_mobile_menu"></div>
                        <div class="dia-mobile_menu_content">
                            <div class="dia-mobile_menu_close dia-open_mobile_menu">
                                <i class=""><img src="<?php echo base_url(); ?>assets/img/cancel-btn-icon.png"></i>
                            </div>
                            <div class="m-brand-logo text-center">
                                <a href="#"><img class="lazyload" src="<?php echo base_url(); ?>assets/img/logo-2.png" alt="matka King"></a>
                            </div>
                            <nav class="dia-mobile-main-navigation  clearfix ul-li">
                               <ul id="m-main-nav" class="navbar-nav text-capitalize clearfix">
                                <li> <a href="#">Home</a></li>
                                <li><a href="#dia-service">About Us</a></li>
                                <li><a href="#dia-about">Live Chart</a></li>
                                <li><a href="#dia-portfolio">How to Play</a></li>
                            </ul>
                        </nav>
                    </div>
                </div>
                <!-- /mobile-menu -->
            </div>
        </div>
    </header>
    <!-- End of header section
        ============================================= -->

    <!-- Start of Banner section
        ============================================= -->
        <section id="dia-banner" class="bg-black dia-banner-section position-relative">
            <div class="banner-side-img banner-img1 position-absolute"><img class="lazyload" src="<?php echo base_url(); ?>assets/img/ns2.png" alt="iReddy Anna king"></div>
            <div class="banner-side-img banner-img2 position-absolute"><img class="lazyload" src="<?php echo base_url(); ?>assets/img/ns13.png" alt="Reddy Anna king"></div>
            <div class="container">
                <div class="dia-banner-content dia-headline pera-content">
                    <span class="dia-banner-tag">We are matka King app!</span>
                    <h1 class="cd-headline clip is-full-width">Download Best matka King app!
          <br>
                        <span class="cd-words-wrapper">
                            <b class="is-visible">Win Money Daily!</b>
                            <b>Try Your Luck</b>
                            <b>Win Everyday</b>
                        </span>
                    </h1>

                    <div class="dia-banner-btn d-flex">
                        <div class="dia-play-btn text-center d-none">
                            <a href="app/matkagame.apk" class=""><img src="<?php echo base_url(); ?>assets/img/play_icon.png"></a>
                        </div>
                        <div class="dia-abt-btn d-none">
                            <a href="app/matkagame.apk"><img src="<?php echo base_url(); ?>assets/img/playstore_icon.png"></a>
                        </div>
            <div class="dia-abt-btn right-btn d-inline-block">
                            <a href="app/matkagame.apk"><img src="<?php echo base_url(); ?>assets/img/android_icon.png"></a>
                        </div>
            <div class="dia-abt-btn right-btn d-inline-block">
                            <a href="https://wa.me/<?=$wtnumber?>?text=Hello%20Admin"><img src="<?php echo base_url(); ?>assets/img/whatsapp_icon.png"></a>
                        </div>
                    </div>
                </div>
            </div>
      
        </section>
    <!-- End of Banner section
        ============================================= -->

    <!-- Start of Service section
        ============================================= -->
<section id="dia-service" class="pt-5 dia-service-section">
            <div class="container">
                <div class="dia-service-content">
                    <div class="row">

                        <div class="col-lg-4 col-md-12">
                            <div class="dia-service-img position-relative">
                                <img class="lazyload" src="<?php echo base_url(); ?>assets/img/s22.png" alt="Indian matka King King">
                                <div class="dia-service-shape dia-service-shape1 position-absolute"><img class="lazyload" src="<?php echo base_url(); ?>assets/img/s2.png" alt=""></div>
                                <div class=" dia-service-shape dia-service-shape2 position-absolute"><img class="lazyload" src="<?php echo base_url(); ?>assets/img/s3.png" alt=""></div>
                            </div>
                        </div>
                        <div class="col-lg-8 col-md-12">
                            <div class="dia-service-text">
                                <div class="dia-section-title text-left text-capitalize dia-headline">
                                    <h2 class="text-black"><b>matka King </b></h2>
                                </div>
                                <div class="dia-service-details clearfix">
                                    <div class="dia-service-item dia-headline ul-li-block wow fadeFromUp" data-wow-delay="0ms" data-wow-duration="1500ms">
                                        <p class="fw-bold text-black">Play matka King has the best matka King platform. You can guess and win large amounts of money through our website. You will receive the best online matka play, including Kalyan tricks as well as tips and tricks to play matka online. Enjoy the best online matka experience and use your skills to win huge rewards. To learn more, call us at the above number.</p>
                    <p class="fw-bold text-black">matka King, winning isn’t always a massive deal. Even if you don’t make it the first time, keep trying for your luck. You will not see your good fortune immediately. Keep trying. Remember, success is not always found in the second attempt. You can achieve success with the help of Madhur Day, Kalyan and matka King. Even if you lose, you’ll still have enough money in your account to continue your betting. Dont let the loss of your first bet discourage you. You will soon be rewarded by luck if you keep trying.</p>

                                    </div>

                                </div>

                                <div class="dia-service-btn">
                                    <div class="dia-service-more text-center float-left">
                                        <a href="app/matkagame.apk">Download Now <svg xmlns="http://www.w3.org/2000/svg" version="1.1" xmlns:xlink="http://www.w3.org/1999/xlink" width="20" height="20" x="0" y="0" viewBox="0 0 512 512" style="enable-background:new 0 0 512 512" xml:space="preserve" class=""><g><path d="M382.56 233.376A15.96 15.96 0 0 0 368 224h-64V16c0-8.832-7.168-16-16-16h-64c-8.832 0-16 7.168-16 16v208h-64a16.013 16.013 0 0 0-14.56 9.376c-2.624 5.728-1.6 12.416 2.528 17.152l112 128A15.946 15.946 0 0 0 256 384c4.608 0 8.992-2.016 12.032-5.472l112-128c4.16-4.704 5.12-11.424 2.528-17.152z" fill="#ffffff" opacity="1" data-original="#000000" class=""></path><path d="M432 352v96H80v-96H16v128c0 17.696 14.336 32 32 32h416c17.696 0 32-14.304 32-32V352h-64z" fill="#ffffff" opacity="1" data-original="#000000" class=""></path></g></svg></a>
                                    </div>
                                    <div class="dia-service-review float-right">
                                        <div class="dia-service-rate ul-li">
                                            <img src="<?php echo base_url(); ?>assets/img/star-icons2.png">
                                            <div class="dia-service-rate-text">
                                                <span>2125 tasks</span> completed by our team
                                                with <span>99%</span> 5-star reviews
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>



<!--JP-->
<div class="row">
    <div class="col-lg-12 col-md-12">
        <div class="dia-section-title text-left text-capitalize dia-headline">
            <br><br>
            <h2 class="text-black">Up Your Guessing Game with the<br><b> matka King</b> Online Play App</h2>
        </div>
        <div class="dia-service-details clearfix">
            <div class="dia-service-item dia-headline ul-li-block wow fadeFromUp" data-wow-delay="0ms" data-wow-duration="1500ms">
                <p class="fw-bold text-black">
                    Since its inception in 1970, matka King has been a popular game primarily because it gives the thrill of both number-based and lottery games. Thanks to technological advancements, gone are the days when you have to find and go to a Satta Bazar to play your hand. matka King brings the Kalyan Bazar to you in an intuitive and interactive landscape. We bring you the convenience of becoming a Reddy Anna King anywhere anytime with just a download of an app.
                    <br><br>
                    Our Indian matka King app is designed in a user-friendly way to ensure that you have the best gaming experience at any time of the day. Additionally, you can find and download our app on the Google Play Store or via APK file free of cost. After the matka King download, you have to follow a simple login procedure and then you’re all set to play the matka King Game.
                </p>
            </div>
        </div>

    </div>
</div>

<!--JP-->
 </div>
</div>
</section>

<section class="py-5">
<div class="row">
    <div class="container">
        <div class="col-12">



<div class="dia-section-title text-left text-capitalize dia-headline">
    <h2>Play matka King More Easily Than Ever</h2>
</div>
<div class="dia-service-details clearfix py-3">
    <div class="dia-service-item dia-headline ul-li-block wow fadeFromUp" data-wow-delay="0ms" data-wow-duration="1500ms">
        <p class="fw-bold text-black">
            At matka King, our aim is to ensure that you have the best experience while playing matka King. This is why we have to tread the extra mile to ensure that our app is easy to use with seamless navigation and interesting features. Heres how you can start playing the game with a few simple steps
            <br><br>
            <b>Step One:</b> Download the matka King app from Google Play Store or through an APK file. Entry to our intuitive Kalyan Bazar is free of cost. While the app is not yet available for iOS users, it will be rolled out soon.
            <br><br>
            <b>Step Two:</b> The next step is to set up an account which will hardly take two minutes. All you have to do is create a user and password and register your email id and phone number. Lastly, youll get a pin on your phone or email id, simply enter that and you are in the online Indian matka King world.
            <br><br>
            <b>Step Three:</b> Youll be welcomed with an informative dashboard thatll show you data about numerical and astrology methods for Milan day, Madhur day, Kalyan, Supreme day, and Rajdhani Main Bazar. Based on your interest you can start the guessing game with just a click of the button. Furthermore, you will get a notification for the matka King result.
            <br><br>
            <b>Note:</b> There are many valuable features within the app that allows the matka King users to make the most out of the technology. Features such as Game Rates, Notices, and How to Play are there to help users get accustomed to the game and get the necessary information to make the best decisions. Additionally, there is a reliable support team that you can contact via email or WhatsApp for any queries.  Dont know how to play matka King or have a technical query? Dont worry weve got your back.
        </p>
    </div>
</div>


<div class="dia-section-title text-left text-capitalize dia-headline">
    <h2>In the Kalyan, Chart Locate Your Lucky Number</h2>
</div>
<div class="dia-service-details clearfix py-3">
    <div class="dia-service-item dia-headline ul-li-block wow fadeFromUp" data-wow-delay="0ms" data-wow-duration="1500ms">
        <p class="fw-bold text-black">
            Do you want to be the Reddy Anna King? If yes, we have designed the best matka King Game app for you to enjoy. We bring the old-world charm of this game into an interactive landscape that is accessible to you anytime and any day. You don’t have to search for anything, as soon as you register, youll see all the guessing game options along with their time.
            <br><br>
            Simply choose the game that intrigues you the most and let your lucky number do the magic. If you are able to choose the right number,  you can enjoy lucrative rewards. Watch out for matka King results, it could be your day to be the King!

        </p>
        <div class="btndownlaod">
                                        <a href="app/matkagame.apk">Download Now <svg xmlns="http://www.w3.org/2000/svg" version="1.1" xmlns:xlink="http://www.w3.org/1999/xlink" width="20" height="20" x="0" y="0" viewBox="0 0 512 512" style="enable-background:new 0 0 512 512" xml:space="preserve" class=""><g><path d="M382.56 233.376A15.96 15.96 0 0 0 368 224h-64V16c0-8.832-7.168-16-16-16h-64c-8.832 0-16 7.168-16 16v208h-64a16.013 16.013 0 0 0-14.56 9.376c-2.624 5.728-1.6 12.416 2.528 17.152l112 128A15.946 15.946 0 0 0 256 384c4.608 0 8.992-2.016 12.032-5.472l112-128c4.16-4.704 5.12-11.424 2.528-17.152z" fill="#ffffff" opacity="1" data-original="#000000" class=""></path><path d="M432 352v96H80v-96H16v128c0 17.696 14.336 32 32 32h416c17.696 0 32-14.304 32-32V352h-64z" fill="#ffffff" opacity="1" data-original="#000000" class=""></path></g></svg></a>
                                    </div>
    </div>
</div>
        </div>
    </div>
</div>
</section>
    <!-- End of Service section
        ============================================= -->

     <!-- Start of How work  section
        ============================================= -->
        <section id="dia-portfolio" class="eg-how-work-section position-relative">
            <div class="how-work-bg-shape position-absolute"><img class="lazyload" src="<?php echo base_url(); ?>assets/img/hws.png" alt=""></div>
            <div class="container">
                <div class="row">
                    <div class="col-lg-7">

                        <div class="ei-how-work-content-item wow fadeFromUp" data-wow-delay="300ms" data-wow-duration="1500ms">
                            <div class="eight-section-title appeight-headline pera-content text-left">
                                <span class="eg-title-tag">
                                    How this app is working?<i class="square-shape"><i></i><i></i><i></i><i></i></i>
                                </span>
                                <h2>This app is working by
                                    <span>some steps!</span>
                                </h2>
                            </div>
                            <!-- /title -->
                            <div id="how-workscrollbar" class="how-work-scroller">
                                <div class="eg-how-work-content">
                                    <div class="eg-how-work-icon-text position-relative">
                                        <span class="scroller-no">1</span>
                                        <!--<div class="eg-how-work-icon float-left text-center">
                                            <i class="flaticon-verified-user"></i>
                                        </div>-->
                                        <div class="eg-how-work-text appeight-headline pera-content">
                                            <h3>Make a profile</h3>
                                            <p class="fw-bold text-black">As a app web crawler expert, I help organizations adjusting.</p>
                                        </div>
                                    </div>
                                    <div class="eg-how-work-icon-text position-relative">
                                        <span class="scroller-no">2</span>
                                        <!--<div class="eg-how-work-icon float-left text-center">
                                            <i class="flaticon-download"></i>
                                        </div>-->
                                        <div class="eg-how-work-text appeight-headline pera-content">
                                            <h3>Download it for Free</h3>
                                            <p class="fw-bold text-black">As a app web crawler expert, I help organizations adjusting.</p>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-5">
                        <div class="how-work-mockup position-relative wow fadeFromUp" data-wow-delay="600ms" data-wow-duration="1500ms">
                            <div class="hw-mockup-img">
                                <img class="lazyload" src="<?php echo base_url(); ?>assets/img/starmatka.gif" alt="How to install matka King for online Game?">
                            </div>
                            <div class="hw-shape1 position-absolute" data-parallax='{"x" : 40}'><img class="lazyload" src="<?php echo base_url(); ?>assets/img/fc1.png" alt="matka King matka King"></div>
                            <div class="hw-shape2 position-absolute" data-parallax='{"x" : -30}'><img class="lazyload" src="<?php echo base_url(); ?>assets/img/fc2.png" alt="matka King download"></div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    <!-- End  of How work  section
        ============================================= -->

        <!-- Start of App Download  section
        ============================================= -->
        <section id="ei-appdownload" class="ei-appdownload-section position-relative" data-background="asset/assets/img/app-landing/background/appbg1.png">
            <div class="container">
                <div class="ei-appdownload-content">
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="ei-app-mockup-img  wow fadeFromRight" data-wow-delay="300ms" data-wow-duration="1500ms">
                                <img class="lazyload" src="<?php echo base_url(); ?>assets/img/amu1.png" alt="matka King Result Online">
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="ei-app-down-text wow fadeFromLeft" data-wow-delay="600ms" data-wow-duration="1500ms">
                                <div class="eight-section-title appeight-headline pera-content text-left">
                                    <span class="eg-title-tag">
                                        App Download<i class="square-shape"><i></i><i></i><i></i><i></i></i>
                                    </span>
                                    <h2>matka King <br>
                                        <span>ONLINE GAME PLAY</span>
                                    </h2>
                                </div>
                                <!-- /title -->
                                <div class="row">
                  <div class="col-lg-6 col-md-6 wow fadeInUp" data-wow-delay="0ms" data-wow-duration="1500ms">
                     <div class="pr-mark-feature-innerbox position-relative headline pera-content">
                      <div class="pr-mark-feature-inner-icon d-flex align-items-center justify-content-center  position-absolute">
                        <i class="fa fa-check"></i>
                      </div>
                      <div class="pr-mark-feature-inner-text">
                        <h3><a href="#">Single Digit</a></h3>
                      </div>
                    </div>
                  </div>
                  <div class="col-lg-6 col-md-6 wow fadeInUp" data-wow-delay="0ms" data-wow-duration="1500ms">
                     <div class="pr-mark-feature-innerbox position-relative headline pera-content">
                      <div class="pr-mark-feature-inner-icon d-flex align-items-center justify-content-center  position-absolute">
                        <i class="fa fa-check"></i>
                      </div>
                      <div class="pr-mark-feature-inner-text">
                        <h3><a href="#">Jodi</a></h3>
                      </div>
                    </div>
                  </div>
                  <div class="col-lg-6 col-md-6 wow fadeInUp" data-wow-delay="0ms" data-wow-duration="1500ms">
                     <div class="pr-mark-feature-innerbox position-relative headline pera-content">
                      <div class="pr-mark-feature-inner-icon d-flex align-items-center justify-content-center  position-absolute">
                        <i class="fa fa-check"></i>
                      </div>
                      <div class="pr-mark-feature-inner-text">
                        <h3><a href="#">Single Pana</a></h3>
                      </div>
                    </div>
                  </div>
                  <div class="col-lg-6 col-md-6 wow fadeInUp" data-wow-delay="0ms" data-wow-duration="1500ms">
                     <div class="pr-mark-feature-innerbox position-relative headline pera-content">
                      <div class="pr-mark-feature-inner-icon d-flex align-items-center justify-content-center  position-absolute">
                        <i class="fa fa-check"></i>
                      </div>
                      <div class="pr-mark-feature-inner-text">
                        <h3><a href="#">Double pana</a></h3>
                      </div>
                    </div>
                  </div>
                  <div class="col-lg-6 col-md-6 wow fadeInUp" data-wow-delay="0ms" data-wow-duration="1500ms">
                     <div class="pr-mark-feature-innerbox position-relative headline pera-content">
                      <div class="pr-mark-feature-inner-icon d-flex align-items-center justify-content-center  position-absolute">
                        <i class="fa fa-check"></i>
                      </div>
                      <div class="pr-mark-feature-inner-text">
                        <h3><a href="#">Triple Pana</a></h3>
                      </div>
                    </div>
                  </div>
                  <div class="col-lg-6 col-md-6 wow fadeInUp" data-wow-delay="0ms" data-wow-duration="1500ms">
                     <div class="pr-mark-feature-innerbox position-relative headline pera-content">
                      <div class="pr-mark-feature-inner-icon d-flex align-items-center justify-content-center  position-absolute">
                        <i class="fa fa-check"></i>
                      </div>
                      <div class="pr-mark-feature-inner-text">
                        <h3><a href="#">Sangam Half</a></h3>
                      </div>
                    </div>
                  </div>
                  <div class="col-lg-6 col-md-6 wow fadeInUp" data-wow-delay="0ms" data-wow-duration="1500ms">
                     <div class="pr-mark-feature-innerbox position-relative headline pera-content">
                      <div class="pr-mark-feature-inner-icon d-flex align-items-center justify-content-center  position-absolute">
                        <i class="fa fa-check"></i>
                      </div>
                      <div class="pr-mark-feature-inner-text">
                        <h3><a href="#">Sangam Full</a></h3>
                      </div>
                    </div>
                  </div>
                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="ei-appdownloaad-shape app-shape1" data-parallax='{"y" : -100}'><img class="lazyload" src="<?php echo base_url(); ?>assets/img/apps1.png" alt="matka King rajdhani game"></div>
            <div class="ei-appdownloaad-shape app-shape2" data-parallax='{"x" : -120}'><img class="lazyload" src="<?php echo base_url(); ?>assets/img/apps2.png" alt="download"></div>
            <div class="ei-appdownloaad-shape app-shape3" data-parallax='{"y" : -100}'><img class="lazyload" src="<?php echo base_url(); ?>assets/img/apps3.png" alt="matka King"></div>
        </section>
    <!-- End of App Download section
        ============================================= -->

    <!-- Start of Experience section
        ============================================= -->
 <section id="dia-about" class="newbg saas_two_service_section">
    <div class="container">
        <div class="saas_two_section_title saas2-headline text-center">
            <span class="title_tag">Our awesome services</span>
                <h2>matka King<br> <span>ONLINE PLAY RESULT</h2>
            </span>
        </div>
        <!-- /section title -->
        <div class="service_content">
            <div class="row">
                <!-- /service-content -->
                    <?php while ($row = mysqli_fetch_array($sff)) { ?>
                <div class="col-lg-6 col-md-12  wow fadeFromUp" data-wow-delay="0ms" data-wow-duration="1500ms">
                    <div class="service_content_box relative-position">
                        <div class="d-flex">
                            <a href="#">
                                <img  src="<?php echo base_url(); ?>assets/img/open-clock-icon.jpg"><?php echo $row['open_time']; ?><br>
                                <img  src="<?php echo base_url(); ?>assets/img/close-clock-icon.jpg"><?php echo $row['close_time']; ?></a>
                                <a href="#" class="ml-auto"><img src="<?php echo base_url(); ?>assets/img/message-icon.jpg"><?php echo $cur_date_1; ?>
                            </a>
                        </div>

                        <div class="service_text_box saas2-headline pera-content">
                            <h3 class="text-center"><?php echo $row['game_name']; ?></h3>
                            <?php $open_number="***";
                            $close_number="***";
                            $close_result="*";
                            $open_result="*";?>
                                <?php
                                $sql1="SELECT tb_game_result_history.* FROM `tb_game_result_history` WHERE result_date ='".$cur_date."' AND game_id='".$row['game_id']."'";
                                                     $sff1 = mysqli_query($conn, $sql1);
                                                     while( $row1=mysqli_fetch_array($sff1)){ ?>


                                                   <?php    if($row1['open_number']!='') {
                                                   $open_number = $row1['open_number'];
                                                            $open_num=$row1['open_number'][0]+$row1['open_number'][1]+$row1['open_number'][2];
                                                            if($open_num<10)
                                                                    $open_result =$open_num;
                                                                if($open_num>9)
                                                                $open_result=$open_num%10;
                                                    }else{
                                                        $open_result="*";
                                                    }

                                                        if($row1['close_number']!='') {
                                                        $close_number=$row1['close_number'];
                                                        $close_num=$row1['close_number'][0]+$row1['close_number'][1]+$row1['close_number'][2];
                                                            if($close_num<10)
                                                                $close_result=$close_num;
                                                            if($close_num>9)
                                                                $close_result=$close_num%10;
                                                    }else{
                                                    $close_result="*";
                                                    }

                                                    ?>
                                                    <?php } ?>

                            <div class="d-flex">
                                <div class="">



                  <p style="font-size:22px;color:blue;">Open Result </p>
                                        <div class="price-box text-center position-relative">

                                                <?php $open_number ; ?>
                                        </div>
                                </div>
                <div class="ml-auto">
                    <p style="font-size:22px;color:blue;">Jodi Number </p>
                                        <div class="price-box text-center position-relative">
                                            <?php   echo "$open_result"."$close_result"; ?>
                                        </div>
                                </div>
                <div class="ml-auto">
                    <p style="font-size:22px;color:blue;">Close Result </p>
                    <div class="price-box text-center position-relative">
                      <?php echo $close_number; ?>
                    </div>
                          </div>
              </div>


                        </div>
                    </div>


                </div>
                <?php    } ?>
            </div>
        </div>
        <div class="service_read_more text-center">
            <a href="#dia-banner">Want to download this app? </a>
        </div>
    </div>
</section>



    <!-- End of Experience section
        ============================================= -->



    <!-- Start of testimonial section
        ============================================= -->
        <section id="dia-testimonial" class="dia-testimonial-section position-relative">
            <div class="tst-side-shape position-absolute" data-parallax='{"y" : 50}'> <img class="lazyload" src="<?php echo base_url(); ?>assets/img/tsts1.png" alt="matka result"></div>
            <div class="container">
                <div class="dia-section-title text-center text-capitalize dia-headline">
                    <h2 class="text-black">GAME TIME TABLE</h2>
                </div>
                <div  class="dia-testimonial_slider-area position-relative">
                    <div class="test-shape1 position-absolute wow fadeFromRight" data-wow-delay="300ms" data-wow-duration="1500ms"> <img class="lazyload" src="<?php echo base_url(); ?>assets/img/tbg1.png" alt="matka King Kalyan game"></div>
                    <div class="test-shape2 position-absolute wow fadeFromLeft" data-wow-delay="300ms" data-wow-duration="1500ms"> <img class="lazyload" src="<?php echo base_url(); ?>assets/img/tbg2.png" alt="matka King game"></div>
                    <div id="dia-testimonial_slide" class="carousel slide" data-ride="carousel" >

                        <div class="carousel_preview">
                            <div class="carousel-inner relative-position">
                                <div class="carousel-item active">
                                    <div class="dia-testimonial_content">
                                       <div class="row">
            <div class="col-lg-12">
            <div class="table-responsive time-table">
                <table class="table table-striped">
              <tbody>
                <tr>
                  <th>Market</th>
                  <th>open</th>
                  <th>close</th>
                  <th>Results</th>
                 </tr>
                                <tr>
                  <td>Kalyan SAVERA</td>
                  <td>09:30 AM</td>
                  <td>11:00 AM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>MILAN MORNING</td>
                  <td>10:10 AM</td>
                  <td>11:15 AM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>Kalyan MORNING</td>
                  <td>10:55 AM</td>
                  <td>11:55 AM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>MADHUR MORNING</td>
                  <td>11:25 AM</td>
                  <td>12:25 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>SRIDEVI</td>
                  <td>11:30 AM</td>
                  <td>12:30 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>TIME BAZAR</td>
                  <td>12:50 PM</td>
                  <td>01:50 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>MADHUR DAY</td>
                  <td>01:20 PM</td>
                  <td>02:20 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>NEW Kalyan</td>
                  <td>01:30 PM</td>
                  <td>02:30 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>RAJDHANI DAY</td>
                  <td>2:55 PM</td>
                  <td>4:55 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>MILAN DAY</td>
                  <td>03:00 PM</td>
                  <td>05:00 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>SUPREME DAY</td>
                  <td>03:25 PM</td>
                  <td>05:25 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>Kalyan</td>
                  <td>04:00 PM</td>
                  <td>06:00 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>TIGER BAZAR</td>
                  <td>06:30 PM</td>
                  <td>07:30 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>SRIDEVI NIGHT</td>
                  <td>6:55 PM</td>
                  <td>7:55 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>NEW MAIN MUMBAI</td>
                  <td>08:10 PM</td>
                  <td>09:10 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>MADHUR NIGHT</td>
                  <td>08:25 PM</td>
                  <td>10:25 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>SUPREME NIGHT</td>
                  <td>8:35 PM</td>
                  <td>10:35 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>MILAN NIGHT</td>
                  <td>8:50 PM</td>
                  <td>10:50 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>RAJDHANI NIGHT</td>
                  <td>09:20 PM</td>
                  <td>11:35 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>Kalyan NIGHT</td>
                  <td>09:20 PM</td>
                  <td>11:20 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>
                                <tr>
                  <td>MAIN BAZAR</td>
                  <td>09:35 PM</td>
                  <td>11:50 PM</td>
                  <td><a href="#" >View Chart</a></td>
                </tr>

                              </tbody>
            </table>
          </div>
        </div>
        </div>
                                    </div>
                                </div>

                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    <!-- End of testimonial section
        ============================================= -->

    <!-- Start of blog section
        ============================================= -->
        <section id="ei-faq" class="ei-faq-section position-relative">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <div class="ei-faq-content">
                            <div class="ei-title-faq pera-content">
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="eight-section-title appeight-headline pera-content text-center">
                                            <span class="eg-title-tag">
                                                Frequently asked Question<i class="square-shape"><i></i><i></i> <i></i><i></i></i>
                                            </span>
                                            <h2>Learn about features
                                                <span>from FAQ!</span></h2>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                                <!-- /title -->
                                <div class="ei-faq-queans">
                                    <div class="accordion" id="accordionExample">
                                        <div class="ei-faq faq_bg">
                                            <div class="ei-faq-header" id="headingOne">
                                                <button class="" data-toggle="collapse" data-target="#collapseOne" aria-controls="collapseOne">
                                                    What is matka King?
                                                </button>
                                            </div>
                                            <div id="collapseOne" class="collapse show" data-parent="#accordionExample">
                                                <div class="ei-faq-body">
                                                    matka King Game is a game of perfect guess and a game of applying your Luck. People play the matka King game with utmost passion and an oversized scale. This game is quite fashionable as it is a game of mind it will hold you until the end.
                                                </div>
                                            </div>
                                        </div>
                                        <div class="ei-faq">
                                            <div class="ei-faq-header" id="headingtwo">
                                                <button class="collapsed" data-toggle="collapse" data-target="#collapsetwo" aria-controls="collapsetwo">
                                                   What is the History of matka King?
                                                </button>
                                            </div>
                                            <div id="collapsetwo" class="collapse" data-parent="#accordionExample">
                                                <div class="ei-faq-body">
                                                    For the first time, the game was played around 1961 in the Worli region of India. During the 1980s And 1990s this game become so popular. This is considered as one of the most exciting and very loving games in which you can win cash prize just by playing the game. So this makes the matka King game growing so fast in India.
                                                </div>
                                            </div>
                                        </div>
                                        <div class="ei-faq">
                                            <div class="ei-faq-header" id="headingthree">
                                                <button class="collapsed" data-toggle="collapse" data-target="#collapsethree" aria-controls="collapsethree">
                                                    Why matka King is popular in India?
                                                </button>
                                            </div>
                                            <div id="collapsethree" class="collapse" data-parent="#accordionExample">
                                                <div class="ei-faq-body">
                                                    In India, matka King game is that enables you to win an outsized chunk of cash. In this game, you can win money through speculations. Yes, this game has now become popular as one of the most lotteries winning game in the country.
                                                </div>
                                            </div>
                                        </div>
                                        <div class="ei-faq">
                                            <div class="ei-faq-header" id="headingfourtype">
                                                <button class="collapsed" data-toggle="collapse" data-target="#collapsefourtype" aria-controls="collapsefour">
                                                    What are the types of matka King?
                                                </button>
                                            </div>
                                            <div id="collapsefourtype" class="collapse" data-parent="#accordionExample">
                                                <div class="ei-faq-body">
                                                    Here are a few well known or most popular matka King games;
                          <ul>
                          <li>Matka: A game in which a Matka is used to draw numbers, the word Matka is a Hindi word which English meaning is earthen pot. As to play this game we use this so we called it a Matka game.  </li>
                          <li> Single: This kind of matka King game is based on number Guessing. Yes, you have to choose a number in between 0 to 9 and betting over that number.  </li>
                          <li> Jodi/Pair: This is also a number guessing game like Single. But in this case, you have to bet over any pair of digits from 00 to 99. </li>
                          <li> Patti/Panna: There is another variant in the list called Patti and Panna. This is the result of some three digits which come as a result of betting and thus the smartest thing is that every three-digit number is Patti/Panna but you only use a limited three-digit number together. And this increases the range of the game. </li>
                          <li> Open Result / Close Result: And it is full of enthusiasm. You may not know but you find two parts in the results of this matka King game. </li>

                          </ul>
                                                </div>
                                            </div>
                                        </div>


                                        <div class="ei-faq">
                                            <div class="ei-faq-header" id="headingfour">
                                                <button class="collapsed" data-toggle="collapse" data-target="#collapsefour" aria-controls="collapsefour">
                                                    How can I play satta online?

                                                </button>
                                            </div>
                                            <div id="collapsefour" class="collapse" data-parent="#accordionExample">
                                                <div class="ei-faq-body">
                          <ul>
                          <li>Thanks to digital transformation, playing matka King has become easier than ever. With our matka King app, we bring you an entertaining and engaging world of matka King guessing games. With a simple matka King download, you can enjoy the game anywhere anytime, and become the Reddy Anna King. Moreover, downloading this app will not cost you anything.</li>
                          </ul>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="ei-faq">
                                            <div class="ei-faq-header" id="headingfourbet">
                                                <button class="collapsed" data-toggle="collapse" data-target="#collapsefourbet" aria-controls="collapsefour">
                                                    How can I bet Matka?
                                                </button>
                                            </div>
                                            <div id="collapsefourbet" class="collapse" data-parent="#accordionExample">
                                                <div class="ei-faq-body">

                                                    <ul>
                                                    <li>The Matka guessing game is based on a random selection of numbers and placing your bets. Players have to choose a number between 0 to 9 and lastly, the chit is drawn digitally to reveal the winners. In the Starr Kalyan app, all the games are mentioned in the dashboard, so you can directly choose the game and start Matka guessing without hassle. You will be shown the Reddy Anna results after the game time is up. </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="ei-faq">
                                            <div class="ei-faq-header" id="headingfourcal">
                                                <button class="collapsed" data-toggle="collapse" data-target="#collapsefourcal" aria-controls="collapsefour">
                                                    How is Satta king number calculated?
                                                </button>
                                            </div>
                                            <div id="collapsefourcal" class="collapse" data-parent="#accordionExample">
                                                <div class="ei-faq-body">

                                                    <ul>
                                                    <li>Winning in Reddy Anna is all about how well you are at guessing the numbers. There will be two draws and in the first draw, you have to pick three numbers from 0 to 9.  The final number that comes out is the addition of all the three numbers that you have chosen. For instance, if you have selected 2, 5, and 6 and the final number would be 13. And, if you want to use only one digit of the final number then it would be 4 and your first draw will look something like this- 2, 5, and 6 x 3.</li>
                                                    <li>
                                                        Coming to the second pick of number, is drawn the same way as the first day. You can choose any random number for example 2, 4, and 8. Adding them together will you a sum of 13. Out of the final number, you will use the last digit, hence your final pick will look be 2, 4, and, 8 x3.
                                                    </li>
                                                    <li>
                                                        In the end, your card will appear like= (2, 5, and 6 x 3) X (2, 4, and, 8 x3)

                                                    </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>




<div class="ei-faq">
    <div class="ei-faq-header" id="headingfourgtype">
        <button class="collapsed" data-toggle="collapse" data-target="#collapsefourgtype" aria-controls="collapsefour">
            How many types of Matka gambling games are there?
        </button>
    </div>
    <div id="collapsefourgtype" class="collapse" data-parent="#accordionExample">
        <div class="ei-faq-body">

            <ul>
            <li>
                There are two types of Matka gambling games which include Reddy Anna and Worli Matka. While the Reddy Anna runs throughout the week, Worli Matka runs only 5 days a week (Monday to Friday).
            </li>
            </ul>
        </div>
    </div>
</div>


<div class="ei-faq">
    <div class="ei-faq-header" id="headingfourking">
        <button class="collapsed" data-toggle="collapse" data-target="#collapsefourking" aria-controls="collapsefour">
            Who is the real Matka King?
        </button>
    </div>
    <div id="collapsefourking" class="collapse" data-parent="#accordionExample">
        <div class="ei-faq-body">

            <ul>
            <li>
            Ratan Khatri is known as the Matka King and he controlled the nationwide illegal gambling networks between 1960 to 1990. He had many international connections and hundreds of punters and dealers spawned across the globe.
            </li>
            </ul>
        </div>
    </div>
    <div class="btndownlaod">
                                        <a href="app/matkagame.apk">Download Now <svg xmlns="http://www.w3.org/2000/svg" version="1.1" xmlns:xlink="http://www.w3.org/1999/xlink" width="20" height="20" x="0" y="0" viewBox="0 0 512 512" style="enable-background:new 0 0 512 512" xml:space="preserve" class=""><g><path d="M382.56 233.376A15.96 15.96 0 0 0 368 224h-64V16c0-8.832-7.168-16-16-16h-64c-8.832 0-16 7.168-16 16v208h-64a16.013 16.013 0 0 0-14.56 9.376c-2.624 5.728-1.6 12.416 2.528 17.152l112 128A15.946 15.946 0 0 0 256 384c4.608 0 8.992-2.016 12.032-5.472l112-128c4.16-4.704 5.12-11.424 2.528-17.152z" fill="#ffffff" opacity="1" data-original="#000000" class=""></path><path d="M432 352v96H80v-96H16v128c0 17.696 14.336 32 32 32h416c17.696 0 32-14.304 32-32V352h-64z" fill="#ffffff" opacity="1" data-original="#000000" class=""></path></g></svg></a>
                                    </div>
</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <span class="ei-faq-shape fq-shape-style1" data-parallax='{"x" : 50}'><img class="lazyload" src="<?php echo base_url(); ?>assets/img/fq-shape1.png" alt="About matka King Game"></span>
                <span class="ei-faq-shape fq-shape-style2"><img class="lazyload" src="<?php echo base_url(); ?>assets/img/fq-shape2.png" alt=""></span>

                <span class="ei-faq-shape fq-shape-style5"><img class="lazyload" src="<?php echo base_url(); ?>assets/img/fq-shape5.png" alt=""></span>
                <span class="ei-faq-shape fq-shape-style6"><img class="lazyload" src="<?php echo base_url(); ?>assets/img/fq-shape6.png" alt="Reddy Anna King"></span>


                </section>
    <!-- End of Blog section
        ============================================= -->

    <!-- Start of newslatter section
        ============================================= -->
        <section id="dia-newslatter" class="dia-newslatter-section position-relative">

            <div class="newslatter-shape position-absolute n-shape2" data-parallax='{"x" : -30}'><img class="lazyload" src="<?php echo base_url(); ?>assets/img/nv2.png" alt="Reddy Anna result"></div>

            <div class="newslatter-shape position-absolute n-shape4" data-parallax='{"y" : 30}'><img class="lazyload" src="<?php echo base_url(); ?>assets/img/nv4.png" alt="Kalyan bazar"></div>
            <div class="newslatter-shape position-absolute n-shape5" data-parallax='{"y" : 30}'><img class="lazyload" src="<?php echo base_url(); ?>assets/img/nv5.png" alt=""></div>
            <div class="newslatter-shape position-absolute n-shape6" data-parallax='{"x" : -30}'><img class="lazyload" src="<?php echo base_url(); ?>assets/img/nv6.png" alt=""></div>
            <div class="container">
                <div class="dia-newslatter-content">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="dia-newslatter-text dia-headline pera-content">
                                <h3 class="text-black">DISCLAIMER</h3>
                                <p class="text-black fw-bold">We strictly recommend you to please visit and browse this site on your own risk. All the information available here is strictly for informational purposes and based on astrology and numerology calculations. We are no way associated or affiliated with any illegal Matka business. We abide by rules and regulations of the regions where you are accessing the website. May be it is illegal or banned in your region. If you are using our website despite ban, you will be solely responsible for the damage or loss occurred or legal action taken. Please leave our website immediately if you dont like our disclaimer. Copying any information / content posted on the website is strictly prohibited and against the law.</p>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    <!-- End of newslatter section
        ============================================= -->

    <!-- Start of Footer  section
        ============================================= -->
        <section id="dia-footer" class="bg-black dia-footer-section position-relative">
            <div class="container">
                <div class="row">
                    <div class="col-lg-4">
                        <div class="dia-footer-widget pera-content dia-headline clearfix">
                            <div class="dia-footer-logo">
                                <img class="lazyload" src="<?php echo base_url(); ?>assets/img/logo-2.png" alt="">
                            </div>


                        </div>
                    </div>
                    <div class="col-lg-4"></div>

                    <div class="col-lg-4 text-white" style="position:relative;z-index:2;">
                        <div class="dia-footer-widget dia-headline  ul-li-block clearfix">
                            <h3 class="py-3">Contact:</h3>
                            <h4 class="width:150px;">

                                <i class="fas fa-phone"></i>
                                Call Us: <a href="tel:+91<?=$wtnumber?>">+91-<?=$wtnumber?></a>
                                <span>(Sat - Thursday)</span>
                            </h4>
                            <div class="download-btn">

                            </div>
                            <div class="dia-footer-social">
                               <a href="https://wa.me/<?=$wtnumber?>?text=Hello"><i class="fab fa-facebook-f "></i></a>
                                <a href="https://wa.me/<?=$wtnumber?>?text=Hello"><i class="fab fa-youtube"></i></a>

                                <a href="<?=$telolink?>"><i class="fab fa-telegram-plane"></i></a>
                                <a href="https://wa.me/<?=$wtnumber?>?text=Hello"><i class="fab fa-whatsapp"></i></a>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="dia-footer-copyright">
                <div class="container">
                    <div class="dia-footer-copyright-content">
                        <div class="row">
                            <div class="col-md-4">
                                <div class="dia-copyright-text pera-content">
                                    <p class="text-white fw-bold"> All Right Reserved 2023-24</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="dia-footer-shape3 position-absolute"><img class="lazyload" src="<?php echo base_url(); ?>assets/img/diamap.png" alt=""></div>
        </section>
      <!-- End of Footer  section
        ============================================= -->

 <script src="<?php echo base_url(); ?>assets/js/jquery.js"  defer ></script>
 <!-- JavaScript Bundle with Popper -->

        <script src="<?php echo base_url(); ?>assets/js/bootstrap.min.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/jquery.mCustomScrollbar.concat.min.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/popper.min.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/appear-2.js" defer ></script>

        <script src="<?php echo base_url(); ?>assets/js/wow.min.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/jquery.fancybox.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/tilt.jquery.min.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/owl.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/aos.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/slick.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/jquery.barfiller.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/typer-new.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/odometer.js" defer ></script>

        <script src="<?php echo base_url(); ?>assets/js/parallax-scroll.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/script.js" defer ></script>




        <script src="<?php echo base_url(); ?>assets/js/pagenav.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/bxslider.js" defer ></script>
        <script src="<?php echo base_url(); ?>assets/js/jquery.barfiller.js" defer ></script>

        <script src="<?php echo base_url(); ?>assets/js/swiper.min.js" defer ></script>


        <script src="https://cdnjs.cloudflare.com/ajax/libs/lodash.js/4.17.21/lodash.min.js" integrity="sha512-WFN04846sdKMIP5LKNphMaWzU7YpMyCU245etK3g/2ARYbPK9Ub18eG+ljU96qKRCWh+quCY7yefSmlkQw1ANQ==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>



<!--  <input type="hidden" id="base_url" value="http://sincar.eu/Kalyanstar/Kalyan-star-online-landingpage-html/"> -->

   </body>
</html>
