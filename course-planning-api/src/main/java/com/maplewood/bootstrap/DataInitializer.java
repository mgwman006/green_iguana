package com.maplewood.bootstrap;

import com.maplewood.service.scheduler.SectionGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Application bootstrap component responsible for triggering
 * initial schedule generation at startup.
 *
 * <p>This class delegates all logic to the scheduling engine.</p>
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

  private final SectionGeneratorService sectionGeneratorService;
  @Override
  public void run(String... args)
  {
    sectionGeneratorService.resetSchedule();
    sectionGeneratorService.generateAllSections();
  }
}