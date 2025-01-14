package engine.enums;

/**
 * @author fissban
 */
public enum PlaySoundType
{
	// music
	MUSIC_B01_F("b01_f"),
	MUSIC_B01_S01("b01_s01"),
	MUSIC_B01_S02("b01_s02"),
	MUSIC_B02_F("b02_f"),
	MUSIC_B02_S01("b02_s01"),
	MUSIC_B02_S02("b02_s02"),
	MUSIC_B03_F("b03_f"),
	MUSIC_B03_S01("b03_s01"),
	MUSIC_B03_S02("b03_s02"),
	MUSIC_B04_F("b04_f"),
	MUSIC_B04_S01("b04_s01"),
	MUSIC_B04_S02("b04_s02"),
	MUSIC_B05_F("b05_f"),
	MUSIC_B05_S01("b05_s01"),
	MUSIC_B05_S02("b05_s02"),
	MUSIC_B06_F("b06_f"),
	MUSIC_B06_S01("b06_s01"),
	MUSIC_B06_S02("b06_s02"),
	MUSIC_B07_F("b07_f"),
	MUSIC_B07_S01("b07_s01"),
	MUSIC_BS01_A("bs01_a"),
	MUSIC_BS01_D("bs01_d"),
	MUSIC_BS02_A("bs02_a"),
	MUSIC_BS02_D("bs02_d"),
	MUSIC_BS03_A("bs03_a"),
	MUSIC_BS03_D("bs03_d"),
	MUSIC_CC_01("cc_01"),
	MUSIC_CC_02("cc_02"),
	MUSIC_CC_03("cc_03"),
	MUSIC_CC_04("cc_04"),
	MUSIC_CC_05("cc_05"),
	MUSIC_CC_06("cc_06"),
	MUSIC_CT_DARKELF("ct_darkelf"),
	MUSIC_CT_DWARF("ct_dwarf"),
	MUSIC_CT_ELF("ct_elf"),
	MUSIC_CT_HUMAN("ct_human"),
	MUSIC_CT_ORC("ct_orc"),
	MUSIC_D01_F("d01_f"),
	MUSIC_D01_S01("d01_s01"),
	MUSIC_D01_S02("d01_s02"),
	MUSIC_D02_F("d02_f"),
	MUSIC_D02_S01("d02_s01"),
	MUSIC_D02_S02("d02_s02"),
	MUSIC_DO3_F("d03_f"),
	MUSIC_D03_S01("d03_s01"),
	MUSIC_D03_S02("d03_s02"),
	MUSIC_D04_F("d04_f"),
	MUSIC_D04_S01("d04_s01"),
	MUSIC_D04_S02("d04_s02"),
	MUSIC_D05_F("d05_f"),
	MUSIC_D05_S01("d05_s01"),
	MUSIC_D05_S02("d05_s02"),
	MUSIC_D06_F("d06_f"),
	MUSIC_D06_S01("d06_s01"),
	MUSIC_D06_S02("d06_s02"),
	MUSIC_D07_F("d07_f"),
	MUSIC_D07_S01("d07_s01"),
	MUSIC_D07_S02("d07_s02"),
	MUSIC_D08_F("d08_f"),
	MUSIC_F01_F("f01_f"),
	MUSIC_F01_S01("f01_s01"),
	MUSIC_F02_F("f02_f"),
	MUSIC_F02_S02("f02_s01"),
	MUSIC_F03_F("f03_f"),
	MUSIC_F03_S01("f03_s01"),
	MUSIC_F04_F("f04_f"),
	MUSIC_F04_S01("f04_s01"),
	MUSIC_F05_F("f05_f"),
	MUSIC_F05_S01("f05_s01"),
	MUSIC_F06_F("f06_f"),
	MUSIC_F06_S01("f06_s01"),
	MUSIC_F07_F("f07_f"),
	MUSIC_F07_S01("f07_s01"),
	MUSIC_F08_F("f08_f"),
	MUSIC_F08_S01("f08_s01"),
	MUSIC_F09_F("f09_f"),
	MUSIC_F09_S01("f09_s01"),
	MUSIC_F10_F("f10_f"),
	MUSIC_F10_S01("f10_s01"),
	MUSIC_F11_F("f11_f"),
	MUSIC_F11_S01("f11_s01"),
	MUSIC_F12_F("f12_f"),
	MUSIC_F12_S01("f12_s01"),
	MUSIC_F13_F("f13_f"),
	MUSIC_F13_S01("f13_s01"),
	MUSIC_F14_F("f14_f"),
	MUSIC_F14_S01("f14_s01"),
	MUSIC_F15_F("f15_f"),
	MUSIC_F15_S01("f15_s01"),
	MUSIC_F16_F("f16_f"),
	MUSIC_F16_S01("f16_s01"),
	MUSIC_F17_F("f17_f"),
	MUSIC_F17_S01("f17_s01"),
	MUSIC_INTRO("intro"),
	MUSIC_NT_ADEN("nt_aden"),
	MUSIC_NT_DARKELF("nt_darkelf"),
	MUSIC_NT_DION("nt_dion"),
	MUSIC_NT_DWARF("nt_dwarf"),
	MUSIC_NT_ELF("nt_elf"),
	MUSIC_NT_FLORAN("nt_floren"),
	MUSIC_NT_GIRAN("nt_giran"),
	MUSIC_NT_GLUDIN("nt_gludin"),
	MUSIC_NT_GLUDIO("nt_gludio"),
	MUSIC_NT_HEINESS("nt_heiness"),
	MUSIC_NT_HUNTERS("nt_hunters"),
	MUSIC_NT_ORC("nt_orc"),
	MUSIC_NT_OREN("nt_oren"),
	MUSIC_NT_SPEAKING("nt_speaking"),
	MUSIC_OUTRO("outro"),
	MUSIC_RM01_A("rm01_a"),
	MUSIC_RM02_A("rm02_a"),
	MUSIC_RM03_A("rm03_a"),
	MUSIC_S01_F("s01_f"),
	MUSIC_S0_("s01_s01"),
	MUSIC_S02_F("s02_f"),
	MUSIC_S02_S01("s02_s01"),
	MUSIC_S03_F("s03_f"),
	MUSIC_S03_S01("s03_s01"),
	MUSIC_S04_F("s04_f"),
	MUSIC_S04_S01("s04_s01"),
	MUSIC_S05_F("s05_f"),
	MUSIC_S05_S01("s05_s01"),
	MUSIC_S06_F("s06_f"),
	MUSIC_S06_S01("s06_s01"),
	MUSIC_S07_F("s07_f"),
	MUSIC_S07_S01("s07_s01"),
	MUSIC_S08_F("s08_f"),
	MUSIC_S08_S01("s08_s01"),
	MUSIC_S09_F("s09_f"),
	MUSIC_S09_S01("s09_s01"),
	MUSIC_S10_F("s10_f"),
	MUSIC_S10_S01("s10_s01"),
	MUSIC_S11_F("s11_f"),
	MUSIC_S11_S01("s11_s01"),
	MUSIC_S12_F("s12_f"),
	MUSIC_S12_S01("s12_s01"),
	MUSIC_S13_F("s13_f"),
	MUSIC_S13_S01("s13_s01"),
	MUSIC_S15_D("s15_d"),
	MUSIC_S15_L("s15_l"),
	MUSIC_S16_D("s16_d"),
	MUSIC_S16_L("s16_l"),
	MUSIC_S17_D("s17_d"),
	MUSIC_S17_L("s17_l"),
	MUSIC_S18_D("s18_d"),
	MUSIC_S18_L("s18_l"),
	MUSIC_S19_D("s19_d"),
	MUSIC_S19_L("s19_l"),
	MUSIC_S20_f("s20_f"),
	MUSIC_S21_f("s21_f"),
	MUSIC_S21_S01("s21_s01"),
	MUSIC_S21_S02("s21_s02"),
	MUSIC_S22_f("s22_f"),
	MUSIC_S22_S01("s22_s01"),
	MUSIC_S23_f("s23_f"),
	MUSIC_S23_S01("s23_s01"),
	MUSIC_S24_f("s24_f"),
	MUSIC_S24_S01("s24_s01"),
	MUSIC_S25_f("s25_f"),
	MUSIC_S25_S01("s25_s01"),
	MUSIC_S26_f("s26_f"),
	MUSIC_S26_S01("s26_s01"),
	MUSIC_S27_f("s27_f"),
	MUSIC_SF_P_01("sf_p_01"),
	MUSIC_SF_S_01("sf_s_01"),
	MUSIC_SIEGE_VITORY("siege_victory"),
	MUSIC_SSQ_DAWN_01("ssq_dawn_01"),
	MUSIC_SSQ_DUSK("ssq_dusk_01"),
	MUSIC_SSQ_NEUTRAL("ssq_neutral_01"),
	MUSIC_S_PIRATE("s_pirate"),
	MUSIC_S_RACE("s_race"),
	MUSIC_S_TOWER("s_tower"),
	MUSIC_T01_F("t01_f"),
	MUSIC_T01_S01("t01_s01"),
	MUSIC_T02_F("t02_f"),
	MUSIC_T02_S01("t02_s01"),
	MUSIC_T03_F("t03_f"),
	MUSIC_T03_S01("t03_s01"),
	MUSIC_T04_F("t04_f"),
	MUSIC_T04_S01("t04_s01"),
	MUSIC_T05_F("t05_f"),
	MUSIC_T05_S01("t05_s01"),
	MUSIC_T06_F("t06_f"),
	MUSIC_T06_S01("t06_s01"),
	MUSIC_T07_F("t07_f"),
	MUSIC_T07_S01("t07_s01"),
	MUSIC_T08_F("t08_f"),
	MUSIC_T08_S01("t08_s01"),
	MUSIC_T09_F("t09_f"),
	MUSIC_T09_S01("t09_s01"),
	MUSIC_T10_F("t10_f"),
	MUSIC_T10_S01("t10_s01"),
	MUSIC_T11_F("t11_f"),
	MUSIC_T11_S01("t11_s01"),
	MUSIC_T12_F("t12_f"),
	MUSIC_T12_S01("t12_s01"),
	MUSIC_T13_F("t13_f"),
	MUSIC_T13_S01("t13_s01"),
	MUSIC_T14_F("t14_f"),
	MUSIC_T14_S01("t14_s01"),
	MUSIC_T15_F("t15_f"),
	MUSIC_T15_S01("t15_s01"),
	MUSIC_T16_F("t16_f"),
	MUSIC_T16_S01("t16_s01"),
	MUSIC_T17_F("t17_f"),
	MUSIC_T17_S01("t17_s01"),
	MUSIC_T18_F("t18_f"),
	MUSIC_T18_S01("t18_s01"),
	MUSIC_T19_F("t19_f"),
	MUSIC_T19_S01("t19_s01"),
	MUSIC_T20_F("t20_f"),
	MUSIC_T20_S01("t20_s01"),
	MUSIC_T21_F("t21_f"),
	MUSIC_T21_S01("t21_s01"),
	MUSIC_T22_F("t22_f"),
	MUSIC_T22_S01("t22_s01"),
	MUSIC_T23_F("t23_f"),
	MUSIC_T23_S01("t23_s01"),
	MUSIC_T24_F("t24_f"),
	MUSIC_T24_S01("t24_s01"),
	MUSIC_T25_F("t25_f"),
	MUSIC_T25_S01("t25_s01"),
	// systemmsg_e
	SYS_MSG_17("Systemmsg_e.17"),
	SYS_MSG_18("Systemmsg_e.18"),
	SYS_MSG_146("Systemmsg_e.146"),
	SYS_MSG_147("Systemmsg_e.147"),
	SYS_MSG_215("Systemmsg_e.215"),
	SYS_MSG_216("Systemmsg_e.216"),
	SYS_MSG_0217("Systemmsg_e.217"),
	SYS_MSG_218("Systemmsg_e.218"),
	SYS_MSG_219("Systemmsg_e.219"),
	SYS_MSG_221("Systemmsg_e.221"),
	SYS_MSG_345("Systemmsg_e.345"),
	SYS_MSG_346("Systemmsg_e.346"),
	SYS_MSG_386("Systemmsg_e.386"),
	SYS_MSG_387("Systemmsg_e.387"),
	SYS_MSG_389("Systemmsg_e.389"),
	SYS_MSG_390("Systemmsg_e.390"),
	SYS_MSG_391("Systemmsg_e.391"),
	SYS_MSG_393("Systemmsg_e.393"),
	SYS_MSG_394("Systemmsg_e.394"),
	SYS_MSG_395("Systemmsg_e.395"),
	SYS_MSG_406("Systemmsg_e.406"),
	SYS_MSG_599("Systemmsg_e.599"),
	SYS_MSG_602("Systemmsg_e.602"),
	SYS_MSG_630("Systemmsg_e.630"),
	SYS_MSG_631("Systemmsg_e.631"),
	SYS_MSG_632("Systemmsg_e.632"),
	SYS_MSG_633("Systemmsg_e.633"),
	SYS_MSG_634("Systemmsg_e.634"),
	SYS_MSG_635("Systemmsg_e.635"),
	SYS_MSG_702("Systemmsg_e.702"),
	SYS_MSG_731("Systemmsg_e.731"),
	SYS_MSG_732("Systemmsg_e.732"),
	SYS_MSG_733("Systemmsg_e.733"),
	SYS_MSG_736("Systemmsg_e.736"),
	SYS_MSG_740("Systemmsg_e.740"),
	SYS_MSG_747("Systemmsg_e.747"),
	SYS_MSG_783("Systemmsg_e.783"),
	SYS_MSG_784("Systemmsg_e.784"),
	SYS_MSG_785("Systemmsg_e.785"),
	SYS_MSG_787("Systemmsg_e.787"),
	SYS_MSG_788("Systemmsg_e.788"),
	SYS_MSG_789("Systemmsg_e.789"),
	SYS_MSG_790("Systemmsg_e.790"),
	SYS_MSG_791("Systemmsg_e.791"),
	SYS_MSG_792("Systemmsg_e.792"),
	SYS_MSG_793("Systemmsg_e.793"),
	SYS_MSG_803("Systemmsg_e.803"),
	SYS_MSG_804("Systemmsg_e.804"),
	SYS_MSG_806("Systemmsg_e.806"),
	SYS_MSG_807("Systemmsg_e.807"),
	SYS_MSG_808("Systemmsg_e.808"),
	SYS_MSG_809("Systemmsg_e.809"),
	SYS_MSG_930("Systemmsg_e.930"),
	SYS_MSG_931("Systemmsg_e.931"),
	SYS_MSG_964("Systemmsg_e.964"),
	SYS_MSG_965("Systemmsg_e.965"),
	SYS_MSG_966("Systemmsg_e.966"),
	SYS_MSG_1063("Systemmsg_e.1063"),
	SYS_MSG_1113("Systemmsg_e.1113"),
	SYS_MSG_1209("Systemmsg_e.1209"),
	SYS_MSG_1210("Systemmsg_e.1210"),
	SYS_MSG_1211("Systemmsg_e.1211"),
	SYS_MSG_1212("Systemmsg_e.1212"),
	SYS_MSG_1213("Systemmsg_e.1213"),
	SYS_MSG_1214("Systemmsg_e.1214"),
	SYS_MSG_1215("Systemmsg_e.1215"),
	SYS_MSG_1216("Systemmsg_e.1216"),
	SYS_MSG_1217("Systemmsg_e.1217"),
	SYS_MSG_1218("Systemmsg_e.1218"),
	SYS_MSG_1219("Systemmsg_e.1219"),
	SYS_MSG_1233("Systemmsg_e.1233"),
	SYS_MSG_1240("Systemmsg_e.1240"),
	SYS_MSG_1241("Systemmsg_e.1241"),
	SYS_MSG_1254("Systemmsg_e.1254"),
	CHAR_CHANGE("Systemmsg_e.char_change"),
	CHAR_CREATION("Systemmsg_e.char_creation"),
	CHAR_LOBBY("Systemmsg_e.char_lobby"),
	CHAR_START("Systemmsg_e.char_start"),
	// itemsound3
	SYS_BREAK("ItemSound3.sys_break"),
	SYS_BROADCAST("ItemSound3.sys_broadcast"),
	SYS_CARACTER_FAILED("ItemSound3.sys_character_failed"),
	SYS_CHAT_PERMISSION("ItemSound3.sys_chat_permission"),
	SYS_CHAR_PROHIBITION("ItemSound3.sys_chat_prohibition"),
	SYS_DENIAL("ItemSound3.sys_denial"),
	SYS_ENCHANT_FAILED("ItemSound3.sys_enchant_failed"),
	SYS_ENCHANT_SUCCES("ItemSound3.sys_enchant_success"),
	SYS_EXCHANGE_SUCCES("ItemSound3.sys_exchange_success"),
	SYS_FAILED("ItemSound3.sys_failed"),
	SYS_FISHING_FAILED("ItemSound3.sys_fishing_failed"),
	SYS_FISHING_SUCCES("ItemSound3.sys_fishing_success"),
	SYS_FRIEND_INVITE("ItemSound3.sys_friend_invite"),
	SYS_FRIEND_LOGIN("ItemSound3.sys_friend_login"),
	SYS_IMPOSSIBLE("ItemSound3.sys_impossible"),
	SYS_MAKE_FAIL("ItemSound3.sys_make_fail"),
	SYS_MAKE_SUCCES("ItemSound3.sys_make_success"),
	SYS_PARTY_INVITE("ItemSound3.sys_party_invite"),
	SYS_PARTY_JOIN("ItemSound3.sys_party_join"),
	SYS_PARTY_LEAVE("ItemSound3.sys_party_leave"),
	SYS_PLEDGE_END("ItemSound3.sys_pledge_end"),
	SYS_PLEDGE_JOIN("ItemSound3.sys_pledge_join"),
	SYS_PLEDGE_LOSE("ItemSound3.sys_pledge_lose"),
	SYS_PLEDGE_START("ItemSound3.sys_pledge_start"),
	SYS_PLEDGE_VICTORY("ItemSound3.sys_pledge_victory"),
	SYS_RECOMMEND("ItemSound3.sys_recommend"),
	SYS_SHORTAGE("ItemSound3.sys_shortage"),
	SYS_SOW_SUCCES("ItemSound3.sys_sow_success"),
	SYS_SPOIL_SUCCES("ItemSound3.sys_spoil_success"),
	SPOIL_SUCCES("ItemSound3.Spoil_Success"),
	// itemsound2
	RACE_START("ItemSound2.ItemSound2.race_start"),
	RACE_END("ItemSound2.ItemSound2.race_end"),
	// itemsound
	SHIP_1MIN("ItemSound.ship_1min"),
	SHIP_5MIN("ItemSoundship_5min"),
	SHIP_ARRIVAL_DEPARTURE("ItemSound.ship_arrival_departure"),
	SIEGE_START("ItemSound.siege_start"),
	SIEGE_END("ItemSound.siege_end"),
	QUEST_ACCEPT("ItemSound.quest_accept"),
	QUEST_ITEMGET("ItemSound.quest_itemget"),
	QUEST_MIDDLE("ItemSound.quest_middle"),
	QUEST_FINISH("ItemSound.quest_finish"),
	QUEST_GIVEUP("ItemSound.quest_giveup"),
	QUEST_JACKPOT("ItemSound.quest_jackpot"),
	QUEST_FANFARE_1("ItemSound.quest_fanfare_1"),
	QUEST_FANFARE_2("ItemSound.quest_fanfare_2"),
	QUEST_FANFARE_EASY("ItemSound.quest_fanfare_easy"),
	QUEST_FANFARE_MIDDLE("ItemSound.quest_fanfare_middle"),
	QUEST_BEFORE_BATTLE("Itemsound.quest_before_battle"),
	QUEST_TUTORIAL("Itemsound.quest_before_battle");
	
	private String soundName;
	
	PlaySoundType(String soundName)
	{
		this.soundName = soundName;
	}
	
	public String getName()
	{
		return soundName;
	}
}
