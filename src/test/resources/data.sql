DELETE FROM tb_avaliacao;
DELETE FROM tb_reserva;
DELETE FROM tb_restaurante;

INSERT INTO tb_restaurante (id, nome, localizacao, tipo_cozinha, horario_funcionamento, quantidade_total_de_mesas)
VALUES (1, 'Restaurante Teste', 'São Paulo', 'Carnes e Churrasco', '10h00 até 22h00', 20);

INSERT INTO tb_reserva (id, data_hora_inicio, data_hora_fim, qtd_pessoas, restaurante_id, nome_cliente, email_cliente, telefone_cliente)
VALUES (1, '2024-03-17 19:00', '2024-03-17 22:00', 4, 1, 'João da Silva', 'joao@gmail.com', '11 99622-3465'),
       (2, '2024-03-18 20:00', '2024-03-18 21:20', 6, 1, 'Manoel Souza', 'manoel@gmail.com', '11 99452-4565');

INSERT INTO tb_avaliacao (id, pontuacao, comentario, restaurante_id, nome_cliente, email_cliente, telefone_cliente)
VALUES (1, 4, 'Ótima Experiência', 1, 'João da Silva', 'joao@gmail.com', '11 99622-3465'),
       (2, 3, 'Deixou a desejar', 1, 'Manoel Souza', 'manoel@gmail.com', '11 99452-4565');



