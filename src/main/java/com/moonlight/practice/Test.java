package com.moonlight.practice;

import lombok.Getter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

public class Test {
	public static void main(String[] args) {
		Timestamp startDate = Timestamp.valueOf("2024-04-01 09:34:03.642000");
		Timestamp endDate = Timestamp.valueOf("2024-04-24 09:34:03.642000");
		System.out.println("startDate : "+startDate);
		System.out.println("End Date : "+endDate);
		Set<String> holidayDates = new HashSet<>(RepoTemp.INSTANCE.allHolidays());
		Duration duration = adjustHolidayDuration(endDate.getTime(), startDate.getTime(), holidayDates);
		System.out.println("holidayAdjusted Day : "+duration.toDays());
	}

	public static Duration adjustHolidayDuration(long startDate, long endDate, Set<String> holidays) {
		if (startDate>endDate) {
			long temp = startDate;
			startDate = endDate;
			endDate = temp;
		}
		Duration totalDuration = Duration.ofMillis(endDate-startDate);
		System.out.println("TotalDays : "+totalDuration.toDays());
		ZoneId zoneId = TimeZone.getTimeZone(AllZone.valueOf("INDIA").getValue()).toZoneId();
		LocalDate startLocalDate = Instant.ofEpochMilli(startDate).atZone(zoneId).toLocalDate();
		LocalDate endLocalDate = Instant.ofEpochMilli(endDate).atZone(zoneId).toLocalDate();
//		LocalDate startLocalDate = start.toLocalDateTime().toLocalDate();
//		LocalDate endLocalDate = end.toLocalDateTime().toLocalDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		int holidayCount = 0;
		LocalDate currentDate = startLocalDate;
		while (!currentDate.isAfter(endLocalDate)) {
			if (holidays.contains(currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))){
				holidayCount++;
			}
			currentDate = currentDate.plusDays(1);
		}
		System.out.println("holiday : "+holidayCount);
		return totalDuration.minusDays(holidayCount);
	}

	@Getter
	enum  AllZone {

		IN("IST"),
		INDIA("IST"),
		SRI_LANKA("IST"),
		AFGHANISTAN("AFT"),
		NEPAL("NPT"),
		BHUTAN("BTT"),
		MYANMAR("MMT"),
		INDONESIA_WEST("WIB"),
		INDONESIA("WIB"),
		INDONESIA_CENTRAL("WITA"),
		INDONESIA_EAST("WIT"),
		THAILAND("ICT"),
		SINGAPORE("SGT"),
		PHILIPPINES("PHT"),
		SOUTH_KOREA("KST"),
		IRAN("IRST"),
		HONG_KONG("HKT"),
		MALAYSIA("MYT"),
		SAUDI_ARABIA("AST"),
		QATAR("AST"),
		ISRAEL("IDT"),
		IRAQ("AST"),
		TAIWAN("CST"),
		CAMBODIA("ICT"),
		MALDIVES("MVT"),
		SYRIA("EET"),
		NORTH_KOREA("KST"),
		UZBEKISTAN("UZT"),
		LAOS("ICT"),
		BRUNEI("BNT"),
		UAE("GST"),
		MONGOLIA_HOVD("HOVT"),
		MONGOLIA_ULAANBAATAR("ULAT"),
		MONGOLIA_CHOIBALSAN("CHOT"),
		LEBANON("EET"),
		YEMEN("AST"),
		MACAU("CST"),
		OMAN("GST"),
		JORDAN("EET"),
		BAHRAIN("AST"),
		KUWAIT("AST"),
		PALESTINE("EEST"),
		KYRGYSTAN("KGT"),
		TURKMENISTAN("TMT"),
		TAJIKISTAN("TJT"),
		TIMOR_LESTE("TLT"),
		NIGERIA("WAT"),
		SOUTH_AFRICA("SAST"),
		SOUTH_AFRICA_EAST("EAT"),
		ALGERIA("CET"),
		ANGOLA("WAT"),
		MOROCCO("WET"),
		CANADA("CST"),
		KENYA("EAT"),
		SUDAN("CAT"),
		TANZANIA("EAT"),
		TOGO("GMT"),
		GHANA("GMT"),
		LIBYA("EET"),
		TUNISIA("CET"),
		UGANDA("EAT"),
		CONGO_KINSHASA("WAT"),
		CONGO_LUBUMBASHI("CAT"),
		MALI("GMT"),
		UK("BST"),
		UNITED_KINGDOM("BST"),
		GERMANY("CEST"),
		FRANCE("CEST"),
		RUSSIA_KALININGRAD("EET"),
		RUSSIA_MOSCOW("MSK"),
		RUSSIA_SAMARA("SAMT"),
		RUSSIA_YEKATERINBURG("YEKT"),
		RUSSIA_OMSK("OMST"),
		RUSSIA_KRASNOYARSK("KRAT"),
		RUSSIA_NOVOSIBIRSK("NOVT"),
		RUSSIA_IRKUTSK("IRKT"),
		RUSSIA_CHITA("YAKT"),
		RUSSIA_VLADIVOSTOK("VLAT"),
		RUSSIA_MAGADAN("MAGT"),
		RUSSIA_YUZHNO_SAKHALINSK("SAKT"),
		RUSSIA_SREDNEKOLYMSK("SRET"),
		RUSSIA_ANADYR("ANAT"),
		RUSSIA_PETROPAVLOVSK_KAMCHATSKY("PETT"),
		US_HONOLULU("HST"),
		UNITED_STATES("USA"),
		US_ANCHORAGE("AKDT"),
		US_LOS_ANGELES("PDT"),
		US_PHOENIX("MST"),
		US_SALT_LAKE_CITY("MDT"),
		US_CHICAGO("CDT"),
		USA("CDT"),
		US_NEW_YORK("EDT"),
		UNITED_ARAB_EMIRATES("AET"),
		AUSTRALIA_DARWIN("ACT"),
		AUSTRALIA_SYDNEY("AET"),
		ARGENTINA("AGT"),
		ALASKA("AST"),
		BRAZIL("BET"),
		AMERICA_CHICAGO("CST"),
		AMERICA_INDIANA("IET"),
		AMERICA_PHOENIX("PNT"),
		AMERICA_PUERTO_RICO("PRT"),
		AMERICA_LOS_ANGELES("PST"),
		EGYPT("ART"),
		POLAND("CET"),
		ZIMBABWE("CAT"),
		ETHIOPIA("EAT"),
		BANGLADESH("BST"),
		CHINA("CTT"),
		JAPAN("JST"),
		ARMENIA("NET"),
		PAKISTAN("PLT"),
		VIETNAM("VST"),
		SAMOA("MIT"),
		NEW_ZEALAND("NST"),
		SWITZERLAND("CET"),
		TURKEY("TRT"),
		NETHERLANDS("CEST");

		private String value;

		AllZone(String value) {
			this.value = value;
		}

	}
}
