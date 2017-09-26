/*
 * Copyright 2017 Makoto Consulting Group, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.makotojava.ncaabb.sqlgenerator;

import java.util.List;

/**
 * Strategy pattern interface.
 * 
 * @author sperry
 *
 */
public interface Strategy {
  final String SCHEMA_NAME = "ncaabb";

  // CONSTANTS for Statistics Categories. We use these to determine which
  /// SQL Generation strategy to employ, which will vary for each type
  /// of data block (i.e., different column values).
  final String STATCAT_TOURNAMENT_RESULTS = "Tournament Results";
  final String STATCAT_TOURNAMENT_PARTICIPANT = "Tournament Participants";

  final String STATCAT_WON_LOST_PERCENTAGE = "Won-Lost Percentage";
  final String STATCAT_SCORING_OFFENSE = "Scoring Offense";
  final String STATCAT_SCORING_DEFENSE = "Scoring Defense";
  final String STATCAT_SCORING_MARGIN = "Scoring Margin";
  final String STATCAT_FIELD_GOAL_PERCENTAGE = "Field-Goal Percentage";
  final String STATCAT_FIELD_GOAL_PERCENTAGE_DEFENSE = "Field-Goal Percentage Defense";
  final String STATCAT_THREE_POINT_FIELD_GOALS_PER_GAME = "Three-Point Field Goals Per Game";
  final String STATCAT_THREE_POINT_PERCENTAGE = "Three-Point Field-Goal Percentage";
  final String STATCAT_THREE_POINT_PERCENTAGE_DEFENSE = "Three Pt FG Defense";
  final String STATCAT_FREE_THROW_PERCENTAGE = "Free-Throw Percentage";
  final String STATCAT_REBOUND_MARGIN = "Rebound Margin";
  final String STATCAT_ASSISTS_PER_GAME = "Assists Per Game";
  final String STATCAT_ASSIST_TURNOVER_RATIO = "Assist Turnover Ratio";
  final String STATCAT_BLOCKED_SHOTS_PER_GAME = "Blocked Shots Per Game";
  final String STATCAT_STEALS_PER_GAME = "Steals Per Game";
  final String STATCAT_TURNOVERS_PER_GAME = "Turnovers Per Game";
  final String STATCAT_TURNOVER_MARGIN = "Turnover Margin";
  final String STATCAT_PERSONAL_FOULS_PER_GAME = "Personal Fouls Per Game";

  /**
   * Generates SQL for the Statistics Category associated with
   * the Strategy implementation.
   * 
   * @param year
   *          The tournament year the SQL is to be generated for.
   * 
   * @param data
   *          The data to be inserted via SQL.
   * 
   * @return String - the SQL INSERT statements to massage the specified
   *         data into the DB.
   */
  String generateSql(String year, List<String[]> data);

  /**
   * Returns the name of the Strategy implementation.
   * 
   * @return String - the name of this Strategy implementation.
   */
  String getStrategyName();
  
  /**
   * Returns the number of rows processed by this Strategy.
   * 
   * @return int - the number of rows processed
   */
  int getNumberOfRowsProcessed();

}