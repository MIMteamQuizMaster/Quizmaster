INSERT INTO `quizmaster`.`role` (`name`) VALUES ('Student');
INSERT INTO `quizmaster`.`role` (`name`) VALUES ('Teacher');
INSERT INTO `quizmaster`.`role` (`name`) VALUES ('Coordinator');
INSERT INTO `quizmaster`.`role` (`name`) VALUES ('Administrator');
INSERT INTO `quizmaster`.`role` (`name`) VALUES ('Technical Administrator');

INSERT INTO `quizmaster`.`user` (`firstname`) VALUES ('Max');
INSERT INTO `quizmaster`.`user` (`firstname`) VALUES ('ismael');
INSERT INTO `quizmaster`.`user` (`firstname`) VALUES ('Erik');
INSERT INTO `quizmaster`.`user` (`firstname`) VALUES ('Jan');
INSERT INTO `quizmaster`.`user` (`firstname`) VALUES ('MJ');


INSERT INTO `quizmaster`.`user_role` (`user_id`, `role_id`) VALUES ('10000', '1');
INSERT INTO `quizmaster`.`user_role` (`user_id`, `role_id`) VALUES ('10001', '2');
INSERT INTO `quizmaster`.`user_role` (`user_id`, `role_id`) VALUES ('10002', '3');
INSERT INTO `quizmaster`.`user_role` (`user_id`, `role_id`) VALUES ('10003', '4');
INSERT INTO `quizmaster`.`user_role` (`user_id`, `role_id`) VALUES ('10004', '5');

INSERT INTO `quizmaster`.`credentials` (`user_id`, `password`) VALUES ('10004', 'mj');
INSERT INTO `quizmaster`.`credentials` (`user_id`, `password`) VALUES ('10000', 'max');
INSERT INTO `quizmaster`.`credentials` (`user_id`, `password`) VALUES ('10001', 'ismael');
INSERT INTO `quizmaster`.`credentials` (`user_id`, `password`) VALUES ('10002', 'erik');
INSERT INTO `quizmaster`.`credentials` (`user_id`, `password`) VALUES ('10003', 'jan');
