package br.com.hfn.migracaodadosjob.reader;

import java.util.Date;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.validation.BindException;

import br.com.hfn.migracaodadosjob.dominio.Pessoa;

@Configuration
public class ArquivoPessoaReaderConfig {

	@Bean
	public FlatFileItemReader<Pessoa> arquivoPessoaReader(){
		return new FlatFileItemReaderBuilder<Pessoa>()
				.name("arquivoPessoaReader")
				.resource(new FileSystemResource("files/pessoas.csv"))
				.delimited()
				.names("nome","email","dataNascimento","idade","id")
				.addComment("--") // Ignora linhas iniciadas em "--"
				//.targetType(Pessoa.class)
				.fieldSetMapper(fieldSetMapper())
				.build();
	}

	private FieldSetMapper<Pessoa> fieldSetMapper() {
		return new FieldSetMapper<Pessoa>() {

			@Override
			public Pessoa mapFieldSet(FieldSet f) throws BindException {
				Pessoa p = new Pessoa();
				p.setNome(f.readString("nome"));
				p.setEmail(f.readString("email"));
				p.setDataNascimento(new Date(f.readDate("dataNascimento","yyyy-MM-dd HH:mm:ss").getTime()));
				p.setIdade(f.readInt("idade"));
				p.setId(f.readInt("id"));
				return p;
			}
			
		};
	}
}
