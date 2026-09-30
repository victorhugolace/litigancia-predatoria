UPDATE denuncias SET oab_advogado = regexp_replace(upper(trim(oab_advogado)), '[[:space:]/.-]', '', 'g'),
    cpf_cnpj_parte = nullif(regexp_replace(cpf_cnpj_parte, '[[:space:]/.-]', '', 'g'), ''),
    comarca = trim(comarca);
