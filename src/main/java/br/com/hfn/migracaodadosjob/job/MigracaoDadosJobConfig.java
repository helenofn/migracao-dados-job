package br.com.hfn.migracaodadosjob.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@EnableBatchProcessing
@Configuration
public class MigracaoDadosJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Bean
	public Job migracaoDadosJob(
			@Qualifier("migrarPessoasStep") Step migrarPessoasStep,
			@Qualifier("migrarDadosBancariosStep") Step migrarDadosBancariosStep) {
		/*return jobBuilderFactory
				.get("migracaoDadosJob")
				.start(migrarPessoasStep)
				.next(migrarDadosBancariosStep)
				.incrementer(new RunIdIncrementer())
				.build();*/
		//Rodando os steps em paralelo
		return jobBuilderFactory
				.get("migracaoDadosJob")
				.start(stepsParalelos(migrarPessoasStep, migrarDadosBancariosStep))
				.end()// End é necessário pois iniciamos um fluxo (Flow)
				.incrementer(new RunIdIncrementer())
				.build();
	}

	private Flow stepsParalelos(Step migrarPessoasStep, Step migrarDadosBancariosStep) {
		Flow migrarDadosBancariosFLow = new FlowBuilder<Flow>("migrarDadosBancariosFLow")
				.start(migrarDadosBancariosStep)
				.build();
		
		Flow stepsParalelosFlow = new FlowBuilder<Flow>("stepsParalelosFlow")
				.start(migrarPessoasStep)
				.split(new SimpleAsyncTaskExecutor())
				.add(migrarDadosBancariosFLow)
				.build();
		
		return stepsParalelosFlow;
	}
}
