INSERT INTO `role` (`name`)
VALUES
    ("ROLE_ADMIN"),
    ("ROLE_LENDER"),
    ("ROLE_USER");

INSERT INTO `countries` (`name`)
VALUES
    ("France"),
    ("Belgique");

INSERT INTO `address` (`street`, `street_number`, `address_details`, `city`, `postal_code`, `country_id`)
VALUES
    ("903-905 Eget, Avenue",    12, "neque et nunc. Quisque ornare",           "Metz",     "57000",    1),
    ("2 rue de n'importe quoi", 69, "libero nec ligula consectetuer rhoncus.", "BullShit", "32000",    1),
    (null,                      null, null,                                      null,       null,     null),
    ("18 Money Street",         03, "libero nec ligula consectetuer rhoncus.",  "Metz",     "57000",   1),
    (null,                      null,  null,                                      null,       null,    1),
    ("11 Money Street",         57, "libero nec ligula consectetuer rhoncus.",  "Metz",     "57050",   1),
    ("18 Money Street",         96, "libero nec ligula consectetuer rhoncus.",  "Metz",     "57070",   1);

INSERT INTO `user` (`first_name`,`last_name`,`email`,`password`,`phone`,`active`, `address_id`, `role_id`)
VALUES
    ("Toto",    "Titi",      "toto@titi.com",                   "$2a$10$sSBHb2AkCYpluDlvm.JNkOe5cY2qqT12iEoE.J.ANlq4XiH0iOmpu", "04 84 86 01 48", true,  1, 1),
    ("Titi",    "Toto",      "titi@toto.com",                   "$2a$10$sSBHb2AkCYpluDlvm.JNkOe5cY2qqT12iEoE.J.ANlq4XiH0iOmpu", "04 84 86 01 48", true,  1, 1),
    ("Lisandra","Hewitt",    "lisandra3673@yahoo.org",          "RFS93LQF1RG",                                                  "07 71 85 16 03", true,  2, 3),
    ("Petit",   "Wsh",       "petit@truc.com",                   "$2a$10$sSBHb2AkCYpluDlvm.JNkOe5cY2qqT12iEoE.J.ANlq4XiH0iOmpu", "08 56 50 84 72", true,  3, 3),
    ("Megan",   "Franco",    "megan4889@hotmail.couk",          "HFZ58FMT5VD",                                                  "06 51 51 83 38", false, 4, 3),
    ("Avye",    "Cochran",   "avye5882@aol.net",                "XQT45OMS6QU",                                                  "06 38 12 88 87", true,  5, 3),
    ("Mahir",   "Ilijazi",   "mahir.ilijazi@stagiairesmns.fr",  "$2a$10$sSBHb2AkCYpluDlvm.JNkOe5cY2qqT12iEoE.J.ANlq4XiH0iOmpu", "00 00 00 00 00", true,  6, 1);


INSERT INTO `request` (`approved`,`response_date`,`motive`,`admin_id`)
VALUES
    (false, NULL,         "cubilia Curae Donec",                                2),
    (true,  "2022-11-01", "vel turpis. Aliquam adipiscing lobortis",            1),
    (false, "2023-02-27", "mi. Duis risus odio, auctor vitae, aliquet",         2),
    (true,  "2022-11-11", "ac facilisis facilisis, magna tellus faucibus leo,", 1),
    (false, NULL,         "pretium",                                            NULL);


INSERT INTO `payment_type` (`name`)
VALUES
    ("Cash"),
    ("Carte bancaire"),
    ("Cheque"),
    ("Autre");


INSERT INTO `payment` (`amount`,`details`,`payment_type_id`)
VALUES
    (154.00,  "blandit viverra. Donec tempus, ",           1),
    (999.99,  "ullamcorper eu, euismod ac, fermentum vel", 2),
    (4205.00, "nibh dolor, nonummy ac, feugiat non",       2),
    (2951.00, "Quisque fringilla euismod enim. ",          3),
    (444.99,  "ornare sagittis felis. Donec tempor",       3);



INSERT INTO `orders` (`start_date`,`end_date`,`event`,`user_id`,`request_id`, `address_id`)
VALUES
    ("2023-01-29", "2023-05-02", "dui.",                                                         3, 1, 1),
    ("2023-04-06", "2023-06-22", "ligula tortor, dictum eu, placerat eget, venenatis a, magna.", 6, 2, 1),
    ("2023-03-03", "2023-08-12", "convallis dolor. Quisque tincidunt pede ac urna.",             4, 4, 2),
    ("2022-10-27", "2024-03-28", "et",                                                           1, 3, 5),
    ("2023-03-13", "2024-07-07", "odio semper cursus. Integer mollis. Integer tincidunt",        3, 5, 4);


INSERT INTO `licence` (`type`,`description`,`max_number`)
VALUES
    ("Donec tincidunt.", "mollis non,", 1),
    ("at, velit.",       "eu neque",    50),
    ("Maecenas mi",      "Sed diam",    15),
    ("ac facilisis",     "Fusce mi",    100),
    ("tortor, dictum",   "senectus et", 49);


INSERT INTO `manufacturers` (`name`)
VALUES
    ("Ipsum Porta Corp."),
    ("Dignissim Lacus Inc."),
    ("Eros Non Corporation"),
    ("Amet Ornare Lectus Limited"),
    ("Consectetuer Ipsum Inc.");


INSERT INTO `models` (`reference`,`manufacturer_id`)
VALUES
    ("non enim.",       1),
    ("odio. Phasellus", 2),
    ("eros turpis",     3),
    ("Mauris non",      4),
    ("ac tellus.",      1);


INSERT INTO `categories` (`name`)
VALUES
    ("INFORMATIQUE"),
    ("ELECTROMENAGER"),
    ("MOBILIER"),
    ("DIVERS");

INSERT INTO `sub_categories` (`name`,`category_id`)
VALUES
    ("Vidéo",                  1),
    ("Ordinateurs",            1),
    ("Chaises",                3),
    ("Canapes",                3),
    ("Kebab",                  4);


INSERT INTO `items` (`serial_number`,`arrival_date`,`name`,`description`,`existing`,`price_per_day`, `image_url`, `warranty`,`on_maintenance`,`sub_category_id`,`model_id`)
VALUES
    ("FA89FCE7-C3F3-A022-964A-EC5F78319F07", "2023-02-12", "Vidéo projecteur"          , "metus. In nec orci. Donec nibh."    , true, 46  , "https://m.media-amazon.com/images/I/61bKSRv78EL.jpg"                                                         , "2022-11-24", false, 1, 1),
    (null                                  , "2022-11-13", "webcam"                    , "vitae, orci. Phasellus dapibus quam", true, 39  , "https://dlcdnwebimgs.asus.com/gain/818d26a5-2d8b-49e3-8381-86fb5945d8cd/"                                    , "2025-07-05", false, 1, 3),
    ("41165C01-A0D7-43FD-348B-0D8AB32F5825", "2022-07-03", "Ordinateur portable lenovo", "blandit at, nisi. Cum"              , true, 41  , null                                                                                                          , "2022-09-20", true , 2, 5),
    ("B3E1ADCC-4038-AEA7-5BAD-C36647C9329B", "2023-04-05", "Ordinateur portable lenovo", "tellus. Aenean egestas"             , true, 27  , "https://www.ecodair.com/6573-large_default/pc-reconditionne-Lenovo-ThinkPad-L470-CoreI3-8Go-SSD-250Go.jpg"   , "2024-03-11", false, 2, 4),
    ("FD3325C2-9931-342C-8D4A-60C88DED74A4", "2022-10-15", "Ordinateur portable lenovo", "orci."                              , true, 13  , "https://www.grosbill.com/images_produits/00283fd1-760b-43df-b9a1-cef5001df686.jpg"                           , "2022-10-08", false, 2, 2),
    (null                                  , "2022-10-15", "Canapé comfortable"        , "un canapé."                         , true, null, "https://d2ans0z9s1x1c.cloudfront.net/produits/canape-modulable-3-places-orange-cuivre-hove-633ee9caa74a8.jpg", "2022-10-08", false, 4, null);



INSERT INTO `repairer` (`name`,`phone`,`email`, `address_id`)
VALUES
    ("Howard Bird",    "07 25 91 86 36",  "howardbird1641@outlook.net", 1),
    ("Derek Santiago", "08 56 27 83 10",  "dereksantiago@aol.net",      2),
    ("Damon Day",      "07 70 11 72 32",  "damonday@icloud.ca",         3),
    ("Ruby Weber",     "06 84 72 15 56",  "rubyweber295@hotmail.org",   4),
    ("Harlan Juarez",  "08 18 82 12 20",  "harlanjuarez@icloud.com",    5);


INSERT INTO `order_item` (`order_id`,`item_id`, `return_date`, `approved`)
VALUES
    (4, 4, "2023-03-24", true),
    (4, 3, null,         true),
    (4, 2, "2023-03-24", true),
    (4, 1, "2023-03-24", true),
    (1, 2, null,         false),
    (3, 5, "2023-08-12", true),
    (5, 1, null,         false),
    (2, 4, "2023-03-22", true);


INSERT INTO `maintenance` (`date_sent`,`date_received`,`incident`,`repair_cost`,`repairer_id`,`item_id`)
VALUES
    ("2023-02-13", "2023-04-26", "pede sagittis augue,",                           0,   3, 3),
    ("2023-02-07", "2023-07-20", "purus",                                          0,   5, 3),
    ("2023-03-16", "2023-03-24", "felis ullamcorper",                              0,   1, 3),
    ("2023-02-02", "2022-05-24", "vel turpis. Aliquam adipiscing lobortis risus.", 0,   1, 1),
    ("2023-03-20", "2023-03-16", "fermentum convallis ligula.",                    370, 1, 2);
