CREATE TABLE tb_cartao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_cartao VARCHAR(16) NOT NULL,
    senha CHAR(4) NOT NULL, -- Senha somente numerica, porém char para aceitar senhas iniciadas com 0
    saldo DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
