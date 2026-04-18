package com.maplewood.bootstrap;

import com.maplewood.service.enrollment.EnrollmentValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class PrerequisiteClosureInitializer implements CommandLineRunner{

  private final EnrollmentValidationService enrollmentValidationService;

  @Override
  public void run(String... args)
  {
    enrollmentValidationService.buildClosure();
  }
}